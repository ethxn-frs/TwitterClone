
package com.etang.twitterclone.network.services

import com.etang.twitterclone.data.model.Conversation
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ConversationDataService {
    @POST("/conversations")
    suspend fun createConversation(
        @Body request: CreateConversationRequest
    ): Response<Conversation>

    @GET("/conversations/user/{id}")
    suspend fun getUserConversations(
        @Path("id") userId: Int
    ): Response<List<Conversation>>

    @POST("/conversations/{conversationId}/remove-user")
    suspend fun removeUserFromConversation(
        @Path("conversationId") conversationId: Int,
        @Body request: UserConversationRequest
    ): Response<Conversation>

    @POST("/conversations/{conversationId}/add-user")
    suspend fun addUserFromConversation(
        @Path("conversationId") conversationId: Int,
        @Body request: UserConversationRequest
    ): Response<Unit>

    @GET("/conversations/{id}")
    suspend fun getConversationById(
        @Path("id") conversationId: Int
    ): Response<Conversation>

    @DELETE("/conversations/{id}")
    suspend fun deleteConversationById(
        @Path("id") conversationId: Int
    ): Response<Unit>

    @GET("/conversations")
    suspend fun getConversations(): Response<List<Conversation>>
}

data class CreateConversationRequest(
    val creatorId: Int,
    val participantIds: List<Int>
)

data class UserConversationRequest(
    val userId: Int
)
