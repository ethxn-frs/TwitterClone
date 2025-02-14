package com.etang.twitterclone.network.services

import com.etang.twitterclone.data.model.Message
import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.network.dto.auth_dto.LoginDto
import com.etang.twitterclone.network.dto.auth_dto.LoginResponseDto
import com.etang.twitterclone.network.dto.auth_dto.RegisterDto
import com.etang.twitterclone.network.dto.auth_dto.RegisterResponseDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserDataSevice {
    @POST("users/username/search")
    suspend fun searchUser(
        @Body request: UsernameSearchRequest
    ): Response<List<User>>

    @GET("users/{id}/followers")
    suspend fun getUserFollowers(
        @Path("id") idUser: Int
    ): Response<List<User>>

    @GET("users/{id}/following")
    suspend fun getUserFollowing(
        @Path("id") idUser: Int
    ): Response<List<User>>

    @GET("users/{id}/messages")
    suspend fun getUserMessages(
        @Path("id") idUser: Int
    ): Response<List<Message>>

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") idUser: Int
    ): Response<List<User>>

    @PUT("unfollow")
    suspend fun unFollowUser(
        @Body request: UnfollowUser
    ): Response<Unit>

}

data class UsernameSearchRequest(val username: String)
data class UnfollowUser(val followerId: Int, val followingId: Int)