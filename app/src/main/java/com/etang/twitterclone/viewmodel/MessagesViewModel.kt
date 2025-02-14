package com.etang.twitterclone.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etang.twitterclone.data.model.Message
import com.etang.twitterclone.repositories.MessageRepository
import kotlinx.coroutines.launch

class MessagesViewModel() : ViewModel() {


    private val repository = MessageRepository()

    val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages

    fun loadMessagesInConversation(conversationId: Int) {
        viewModelScope.launch {
            try {
                _messages.value = repository.getMessagesInConversation(conversationId)
            } catch (e: Exception) {

            }
        }
    }

    fun sendMessage(conversationId: Int, userId: Int, content: String) {
        viewModelScope.launch {
            try {
                val newMessage = repository.sendMessage(conversationId, userId, content)
                val currentList = _messages.value ?: listOf()
                _messages.value = currentList + newMessage
                //loadMessagesInConversation(conversationId)
            } catch (e: Exception) {

            }
        }
    }

    fun markMessageAsSeen(messageId: Int, userId: Int) {
        viewModelScope.launch {
            try {
                repository.markMessageAsSeen(messageId, userId)
            } catch (e: Exception) {

            }
        }
    }
}