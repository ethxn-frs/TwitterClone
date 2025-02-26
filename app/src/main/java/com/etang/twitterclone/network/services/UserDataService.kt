package com.etang.twitterclone.network.services

import com.etang.twitterclone.data.model.Post
import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.network.dto.SearchRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserDataService {

    @POST("/users/search")
    suspend fun searchUsers(@Body request: SearchRequestDto): Response<List<User>>

    @GET("/users/{id}/posts")
    suspend fun getUserPosts(@Path("id") userId: Int): Response<List<Post>>

    @GET("/users/{id}/comments")
    suspend fun getUserComments(@Path("id") userId: Int): Response<List<Post>>

    @GET("/users/{id}/likes")
    suspend fun getUserLikedPosts(@Path("id") userId: Int): Response<List<Post>>

}