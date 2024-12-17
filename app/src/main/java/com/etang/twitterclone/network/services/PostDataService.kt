package com.etang.twitterclone.network.services

import com.etang.twitterclone.data.model.Post
import com.etang.twitterclone.network.dto.CreatePostRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PostDataService {

    @Headers("Content-Type: application/json")
    @POST("/posts/create")
    suspend fun createPost(@Body request: CreatePostRequest): Response<Unit>

    @GET("/posts")
    suspend fun getPosts(): Response<List<Post>>

    @PUT("posts/{id}/like")
    suspend fun likePost(
        @Path("id") postId: Int, @Body userId: Map<String, Int>
    ): Response<Void>


}