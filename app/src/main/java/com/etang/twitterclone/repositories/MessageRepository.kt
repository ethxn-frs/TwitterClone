package com.etang.twitterclone.repositories

import android.provider.Settings.Global
import com.etang.twitterclone.data.model.Message
import com.etang.twitterclone.network.RetrofitClient
import com.etang.twitterclone.network.services.MarkMessageSeenRequest
import com.etang.twitterclone.network.services.MessageDataService
import com.etang.twitterclone.network.services.SendMessageRequest
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class MessageRepository() {

    private val service = RetrofitClient.instance.create(MessageDataService::class.java)

    suspend fun getMessagesInConversation(conversationId: Int): List<Message> {
        val response = service.getMessagesInConversation(conversationId)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Failed to fetch messages in conversation")
        }
    }

    suspend fun getMessageById(messageId: Int): Message {
        val response = service.getMessageById(messageId)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Failed to fetch message by id")
        }
    }

    fun sendMessage(conversationId: Int, userId: Int, content: String): Deferred<Message> = GlobalScope.async {
        val request = SendMessageRequest(conversationId, userId, content)
        val response = service.sendMessage(request)
        if (response.isSuccessful) {
            return@async response.body()!!
        } else {
            throw Exception("Failed to send message")
        }
    }

    suspend fun markMessageAsSeen(messageId: Int, userId: Int) {
        val request = MarkMessageSeenRequest(userId)
        val response = service.markMessageAsSeen(messageId, request)
        if (!response.isSuccessful) {
            throw Exception("Failed to mark message as seen")
        }
    }

    suspend fun deleteMessageAsSeen(messageId: Int, userId: Int) {
        val request = MarkMessageSeenRequest(userId)
        val response = service.deleteMessageAsSeen(messageId, request)
        if (!response.isSuccessful) {
            throw Exception("Failed to mark message as seen")
        }
    }
    suspend fun deleteMessageId(messageId: Int): Boolean {
        val response = service.deleteMessageId(messageId)
        if (!response.isSuccessful) {
            throw Exception("Failed to mark message as seen")
        }
        return true
    }
}