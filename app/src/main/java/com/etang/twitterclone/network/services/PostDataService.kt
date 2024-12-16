package com.etang.twitterclone.network.services

import com.etang.twitterclone.network.dto.CreatePostRequest
import com.etang.twitterclone.data.model.Post
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface PostDataService {

    @Headers("Content-Type: application/json")
    @POST("/posts/create")
    suspend fun createPost(@Body request: CreatePostRequest): Response<Unit>

    @GET("/posts")
    suspend fun getPosts(): Response<List<Post>>

}