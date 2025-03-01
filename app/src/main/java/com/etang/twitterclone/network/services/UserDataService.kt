package com.etang.twitterclone.network.services

import com.etang.twitterclone.data.model.Post
import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.network.dto.SearchRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserDataService {

    @POST("/users/search")
    suspend fun searchUsers(@Body request: SearchRequestDto): Response<List<User>>

    @GET("/users/{id}/posts")
    suspend fun getUserPosts(@Path("id") userId: Int): Response<List<Post>>

    @GET("/users/{id}/comments")
    suspend fun getUserComments(@Path("id") userId: Int): Response<List<Post>>

    @GET("/users/{id}/likes")
    suspend fun getUserLikedPosts(@Path("id") userId: Int): Response<List<Post>>

    @GET("/users/{id}")
    suspend fun getUserById(@Path("id") userId: Int): Response<User>

    @PUT("/follow")
    suspend fun followUser(@Body requestBody: Map<String, Int>): Response<Void>

    @PUT("/unfollow")
    suspend fun unfollowUser(@Body requestBody: Map<String, Int>): Response<Void>

    @GET("/isFollowing")
    suspend fun isFollowing(
        @Query("followerId") followerId: Int,
        @Query("followeeId") followeeId: Int
    ): Response<IsFollowingResponse>

    @PATCH("/users/{id}")
    suspend fun updateUserField(
        @Path("id") userId: Int,
        @Body updates: Map<String, String>
    ): Response<Void>
}

data class IsFollowingResponse(val isFollowing: Boolean)
