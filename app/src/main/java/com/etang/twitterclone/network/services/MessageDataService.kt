package com.etang.twitterclone.network.services

import com.etang.twitterclone.data.model.Message
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MessageDataService {
    @GET("messages/conversation/{id}")
    suspend fun getMessagesInConversation(
        @Path("id") conversationId: Int
    ): Response<List<Message>>

    @GET("messages/{id}")
    suspend fun getMessageById(
        @Path("id") messageId: Int
    ): Response<Message>

    @POST("messages")
    suspend fun sendMessage(
        @Body request: SendMessageRequest
    ): Response<Message>

    @PUT("messages/seenBy/{id}")
    suspend fun markMessageAsSeen(
        @Path("id") messageId: Int,
        @Body request: MarkMessageSeenRequest
    ): Response<Unit>
}

data class SendMessageRequest(
    val conversationId: Int,
    val userId: Int,
    val content: String
)

data class MarkMessageSeenRequest(
    val userId: Int
)