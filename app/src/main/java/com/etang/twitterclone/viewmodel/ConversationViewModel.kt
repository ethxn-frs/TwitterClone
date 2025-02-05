package com.etang.twitterclone.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etang.twitterclone.data.model.Conversation
import com.etang.twitterclone.repositories.ConversationRepository
import kotlinx.coroutines.launch

class ConversationViewModel() : ViewModel() {

    private val repository = ConversationRepository()
    private val _userConversations = MutableLiveData<List<Conversation>>()
    val userConversations: LiveData<List<Conversation>> get() = _userConversations

    private val _conversationDetails = MutableLiveData<Conversation?>()
    val conversationDetails: MutableLiveData<Conversation?> get() = _conversationDetails

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error


    fun fetchUserConversations(userId: Int) {
        viewModelScope.launch {
            try {
                 val conversations = repository.getUserConversations(userId)
                _userConversations.value = conversations
            } catch (e: Exception) {
                _error.value = "Failed to load conversations: ${e.message}"
            }
        }
    }

    fun fetchConversationById(conversationId: Int, userId: Int){
        viewModelScope.launch {
            try {
                val conversations = repository.getUserConversations(userId)
                val conv = conversations.firstOrNull{ it.id == conversationId}
                if(conv != null){
                    _conversationDetails.value = conv
                }else{
                    _error.value = "Conversation not found"
                }
            }catch (e: Exception){
                _error.value = "Failed to load conversation details: ${e.message}"
            }
        }
    }

    fun createConversation(userId: Int, participantIds: List<Int>) {
        viewModelScope.launch {
            repository.createConversation(userId, participantIds)

        }
    }
}
