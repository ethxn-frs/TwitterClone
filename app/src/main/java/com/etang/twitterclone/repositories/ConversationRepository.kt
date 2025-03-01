package com.etang.twitterclone.repositories

import android.util.Log
import com.etang.twitterclone.data.model.Conversation
import com.etang.twitterclone.network.RetrofitClient
import com.etang.twitterclone.network.services.ConversationDataService
import com.etang.twitterclone.network.services.CreateConversationRequest
import com.etang.twitterclone.network.services.UserConversationRequest


class ConversationRepository() {

    private val service = RetrofitClient.instance.create(ConversationDataService::class.java)

    suspend fun createConversation(creatorId: Int, participantIds: List<Int>): Conversation {
        val request = CreateConversationRequest(creatorId, participantIds)
        val response = service.createConversation(request)
        Log.d("Creator + participantsIds", creatorId.toString() + participantIds.toString())
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Failed to create conversation")
        }
    }

    suspend fun getUserConversations(userId: Int): List<Conversation> {
        val response = service.getUserConversations(userId)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Failed to fetch user conversations")
        }
    }

    suspend fun getConversationById(conversationId: Int): Conversation{
        val response = service.getConversationById(conversationId)
        if(response.isSuccessful){
            return response.body()!!
        }else{
            throw Exception("Failed to fetch conversation details")
        }
    }

    suspend fun addUserFromConversation(conversationId: Int, userId: Int){
        val request = UserConversationRequest(userId)
        val response = service.addUserFromConversation(conversationId, request)
        if(!response.isSuccessful){
            throw Exception("Echec de l'ajout de l'utilisateur")
        }
    }

    suspend fun removeUserFromConversation(conversationId: Int, userId: Int){
        val request = UserConversationRequest(userId)
        val response = service.removeUserFromConversation(conversationId, request)
        if(!response.isSuccessful){
            throw Exception("Echec de la suppression de l'utilisateur")
        }
    }
}