package com.etang.twitterclone.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etang.twitterclone.data.model.Conversation
import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.repositories.ConversationRepository
import com.etang.twitterclone.repositories.UserRepository
import kotlinx.coroutines.launch

class ConversationViewModel() : ViewModel() {

    private val conversationRepository = ConversationRepository()
    private val userRepository = UserRepository()

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
                val conversations = conversationRepository.getConversationById(conversationId)
                _conversationDetails.value =conversations

                val (creator, participants) = extractCreatorAndParticipants(conversations, currentUserId = userId)
                _creatorUsername.value = creator?.username ?: "Inconnu"
                _participants.value = participants
            }catch (e: Exception){
                _error.value = "Failed to load conversation details: ${e.message}"
            }
        }
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
                participantIds.add(user.id)
            }
            if(participantIds.isNotEmpty()){
                conversationRepository.createConversation(creatorId, participantIds)
                _creationSuccess.value = true
            }else{
                _error.value = "Aucun participant valide"
            }


        }
    }


}
