package com.etang.twitterclone.repositories

import com.etang.twitterclone.data.model.Conversation
import com.etang.twitterclone.network.services.ConversationDataService
import com.etang.twitterclone.network.services.CreateConversationRequest
import com.etang.twitterclone.network.services.UserConversationRequest


class ConversationRepository(private val service: ConversationDataService) {
    suspend fun createConversation(creatorId: Int, participantIds: List<Int>): Conversation{
        val request = CreateConversationRequest(creatorId, participantIds)
        val response = service.createConversation(request)
        if(response.isSuccessful){
            return response.body()!!
        }else{
            throw Exception("Failed to create conversation")
        }
    }
    suspend fun getUserConversation(userId: Int): List<Conversation>{
        val response = service.getUserConversations(userId)
        if(response.isSuccessful){
            return response.body()!!
        }else{
            throw Exception("Failed to fetch user conversations")
        }
    }

    suspend fun removeUserFromConversation(conversationId: Int, userId: Int){
        val request = UserConversationRequest(userId)
        val response = service.removeUserFromConversation(conversationId, request)
        if(!response.isSuccessful){
            throw Exception("Failed to remove user from conversation")
        }
    }
}