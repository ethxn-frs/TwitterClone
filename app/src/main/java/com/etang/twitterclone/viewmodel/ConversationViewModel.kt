package com.etang.twitterclone.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etang.twitterclone.data.model.Conversation
import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.repositories.ConversationRepository
import com.etang.twitterclone.repositories.UserRepository
import com.etang.twitterclone.session.SessionManager
import kotlinx.coroutines.launch

class ConversationViewModel(
    private val sessionManager: SessionManager,
    private val conversationRepository :ConversationRepository,
    private val userRepository: UserRepository) : ViewModel() {

    private val messagesViewModel: MessagesViewModel = MessagesViewModel()

    private val _userConversations = MutableLiveData<List<Conversation>>()
    val userConversations: LiveData<List<Conversation>> get() = _userConversations

    private val _conversationDetails = MutableLiveData<Conversation?>()
    val conversationDetails: MutableLiveData<Conversation?> get() = _conversationDetails

    private val _creatorUsername = MutableLiveData<String>()
    val creatorUsername: LiveData<String> get() = _creatorUsername

    private val _participants = MutableLiveData<List<User>>()
    val participants : LiveData<List<User>> get() = _participants

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _creationSuccess = MutableLiveData<Boolean>()
    val creationSuccess: LiveData<Boolean> get() = _creationSuccess

    private val _createdConversationId = MutableLiveData<Int?>()
    val createdConversationId: LiveData<Int?> get() = _createdConversationId

    fun fetchUserConversations(userId: Int) {
        viewModelScope.launch {
            try {
                 val conversations = conversationRepository.getUserConversations(userId)
                _userConversations.value = conversations
            } catch (e: Exception) {
                _error.value = "Failed to load conversations: ${e.message}"
            }
        }
    }

    fun fetchConversationById(conversationId: Int, userId: Int){
        viewModelScope.launch {
            try {
                val conversations = getConversationById(conversationId)
                _conversationDetails.value = conversations
                val (creator, participants) = extractCreatorAndParticipants(conversations, currentUserId = userId)
                _creatorUsername.value = creator?.username ?: "Inconnu"
                _participants.value = participants
            }catch (e: Exception){
                _error.value = "Failed to load conversation details: ${e.message}"
            }
        }
    }

    suspend fun getConversationById(conversationId: Int): Conversation {
        return conversationRepository.getConversationById(conversationId)
    }

    private fun extractCreatorAndParticipants(conv: Conversation, currentUserId: Int): Pair<User?, List<User>>{
        val creator = conv.users.firstOrNull { it.id != currentUserId}
        val participants = conv.users.filter { it.id != creator?.id }

        return Pair(creator, participants)
    }
    fun createConversation(creatorId: Int, usernames: List<String>) {
        viewModelScope.launch {
            val participantIds = mutableListOf<Int>()
            for(username in usernames){
                val user = userRepository.searchUserByUsername(username)
                if(user == null){
                    _error.value = "L'utilisateur \"$username\" n'éxiste pas"
                    return@launch
                }
                val isFollowing = userRepository.isFollowingUser(creatorId, user.id)
                if(!isFollowing){
                    _error.value = "Vous ne suivez pas \"$username\""
                    return@launch
                }
                val followCreator = isUserFollowingCreator(creatorId, user.id)
                if(!followCreator){
                    _error.value = "L'utilisateur \"$username\" ne vous suit pas en retour"
                    return@launch
                }
                participantIds.add(user.id)
            }
            if(participantIds.isNotEmpty()){
                val conversation = conversationRepository.createConversation(creatorId, participantIds)
                _createdConversationId.postValue(conversation.id)
                _creationSuccess.value = true
            }else{
                _error.value = "Aucun participant valide"
            }


        }
    }

    suspend fun checkIfConversationExists(usernames: List<String>, sessionManager: SessionManager): Boolean {
        try {
            val convs = conversationRepository.getUserConversations(sessionManager.getUserId())
            val currentUserName = sessionManager.getUser()?.username.orEmpty()
            val newUsers = (usernames + currentUserName).toSet()
            for(conv in convs){
                val conversationWithUsers = conversationRepository.getConversationById(conv.id)
                val convUsers = conversationWithUsers.users.map { it.username }.toSet()

                if (convUsers == newUsers){
                    return true
                }
            }
        } catch (e: Exception){
            _error.postValue("Erreur lors de la verification de la conversation : ${e.message}")
        }
        return false
    }

    fun addUserToConversation(conversationId: Int, userId: Int, username: String) {
        viewModelScope.launch {
            try {
                val conversation = conversationRepository.getConversationById(conversationId)
                val currentParticipantsName = conversation.users.map { it.username }
                val currentParticipantsId = conversation.users.map {it.id}

                if(currentParticipantsId.contains(userId)){
                    _error.postValue("L'utilisateur est deja present dans la conversation")
                    return@launch
                }
                for (participantId in currentParticipantsId){
                    val userFollows = userRepository.isFollowingUser(userId, participantId)
                    val participantFollowsUser = userRepository.isFollowingUser(participantId, userId)

                    if(!userFollows || !participantFollowsUser){
                        _error.postValue("L'utilisateur et les participants doivent être abonnés l'un et l'autre")
                        return@launch
                    }

                }
                val check = checkIfConversationsExistsWhenAddingUser(currentParticipantsName, username)
                if(check){
                    _error.postValue("Une conversation identique existe deja vous ne pouvez pas l'ajouter")
                    return@launch
                }
                conversationRepository.addUserFromConversation(conversationId, userId)

                fetchConversationById(conversationId, sessionManager.getUserId())
            } catch (e: Exception) {
                _error.postValue("Erreur lors de l'ajout : ${e.message}")
            }
        }
    }

    suspend fun checkIfConversationsExistsWhenAddingUser(currentParticipantsName: List<String>, username: String): Boolean{
        val convs = conversationRepository.getUserConversations(sessionManager.getUserId())
        val newUsers = (currentParticipantsName + username).toSet()
        for(conv in convs){
            val conversationWithUsers = conversationRepository.getConversationById(conv.id)
            val convUsers = conversationWithUsers.users.map { it.username }.toSet()

            if (convUsers == newUsers){
                return true
            }
        }
        return false
    }

    fun removeUserFromConversation(conversationId: Int, user: User) {
        viewModelScope.launch {
            try {
                val conversation = conversationRepository.getConversationById(conversationId)

                conversation.messages.forEach { message ->
                    messagesViewModel.deleteMessageAsSeen(message.id, user.id)
                }
                conversationRepository.removeUserFromConversation(conversationId, user.id)
                val newConv = conversationRepository.getConversationById(conversationId)
                conversation.messages.forEach { message ->
                    if(message.author.username.contains(user.username)){
                        messagesViewModel.deleteMessageById(message.id)
                    }
                }
                if(newConv.users.size <= 1){
                    conversationRepository.deleteConversationById(newConv.id)
                    _conversationDetails.postValue(null)
                }else{
                    fetchConversationById(newConv.id, sessionManager.getUserId())
                }

            } catch (e: Exception) {
                _error.postValue("Erreur lors de la suppression : ${e.message}")
            }
        }
    }

    suspend fun isUserFollowingCreator(creatorId: Int, targetId: Int): Boolean{
        val followCreator = userRepository.isUserFollowingCreator(creatorId, targetId)
        if(!followCreator){
            return false
        }
        return true
    }


}
