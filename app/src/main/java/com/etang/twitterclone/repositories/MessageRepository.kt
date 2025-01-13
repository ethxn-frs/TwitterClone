package com.etang.twitterclone.repositories

import com.etang.twitterclone.data.model.Message
import com.etang.twitterclone.network.services.MarkMessageSeenRequest
import com.etang.twitterclone.network.services.MessageDataService
import com.etang.twitterclone.network.services.SendMessageRequest

class MessageRepository(private val service: MessageDataService) {
    suspend fun getMessagesInConversation(conversationId: Int): List<Message>{
        val response = service.getMessagesInConversation(conversationId)
        if(response.isSuccessful){
            return response.body()!!
        }else{
            throw Exception("Failed to fetch messages in conversation")
        }
    }
    suspend fun getMessageById(messageId: Int): Message{
        val response = service.getMessageById(messageId)
        if(response.isSuccessful){
            return response.body()!!
        }
        else{
            throw Exception("Failed to fetch message by id")
        }
    }
    suspend fun sendMessage(conversationId: Int, userId: Int, content: String): Message{
        val request = SendMessageRequest(conversationId,userId,content)
        val response = service.sendMessage(request)
        if(response.isSuccessful){
            return response.body()!!
        }
        else{
            throw Exception("Failed to send message")
        }
    }

    suspend fun markMessageAsSeen(messageId: Int, userId: Int){
        val request = MarkMessageSeenRequest(userId)
        val response = service.markMessageAsSeen(messageId, request)
        if(!response.isSuccessful){
            throw Exception("Failed to mark message as seen")
        }
    }
}