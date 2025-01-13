package com.etang.twitterclone.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etang.twitterclone.data.model.Conversation
import com.etang.twitterclone.repositories.ConversationRepository
import kotlinx.coroutines.launch

class ConversationViewModel(private val repository: ConversationRepository) : ViewModel() {

    private val _userConversations = MutableLiveData<List<Conversation>>()
    val userConversations: LiveData<List<Conversation>> get() = _userConversations

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
}
