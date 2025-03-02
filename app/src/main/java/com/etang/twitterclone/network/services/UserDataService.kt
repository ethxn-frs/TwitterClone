package com.etang.twitterclone.network.services

import com.etang.twitterclone.data.model.Message
import com.etang.twitterclone.data.model.Post
import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.network.dto.SearchRequestDto
import com.google.gson.JsonElement
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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
        @Query("followerId") followerId: Int, @Query("followeeId") followeeId: Int
    ): Response<IsFollowingResponse>

    @PATCH("/users/{id}")
    suspend fun updateUserField(
        @Path("id") userId: Int, @Body updates: Map<String, String>
    ): Response<Void>

    @GET("users/{userId}/followers")
    suspend fun getUserFollowers(@Path("userId") userId: Int): Response<List<User>>

    @GET("users/{userId}/following")
    suspend fun getUserFollowings(@Path("userId") userId: Int): Response<List<User>>

    @PUT("users/{useId}/delete-pp")
    fun deleteProfilePicture(@Path("userId") userId: Int)

    @PUT("users/{useId}/delete-cover")
    fun deleteCoverPicture(@Path("userId") userId: Int)

    @Multipart
    @PUT("/users/{userId}/update-pp")
    suspend fun updateProfilePicture(
        @Path("userId") userId: Int,
        @Part file: MultipartBody.Part?
    ): Response<Void>

    @Multipart
    @PUT("/users/{userId}/update-cover")
    suspend fun updateCoverPicture(
        @Path("userId") userId: Int,
        @Part file: MultipartBody.Part?
    ): Response<Void>

    @POST("users/username/search")
    suspend fun searchUser(
        @Body request: UsernameSearchRequest
    ): Response<List<User>>


    @GET("users/{id}/following")
    suspend fun getUserFollowing(
        @Path("id") idUser: Int
    ): Response<List<User>>

    @GET("users/{id}/messages")
    suspend fun getUserMessages(
        @Path("id") idUser: Int
    ): Response<List<Message>>

    @PUT("unfollow")
    suspend fun unFollowUser(
        @Body request: UnfollowUser
    ): Response<Unit>

    @GET("users")
    suspend fun getAllUsers(): Response<JsonElement>
}

data class IsFollowingResponse(val isFollowing: Boolean)
data class UsernameSearchRequest(val username: String)
data class UnfollowUser(val followerId: Int, val followingId: Int)
