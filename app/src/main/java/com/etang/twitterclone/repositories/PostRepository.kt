package com.etang.twitterclone.repositories

import android.util.Log
import com.etang.twitterclone.network.services.TwitterApi
import com.etang.twitterclone.network.dto.CreatePostRequest
import com.etang.twitterclone.data.model.Post
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostRepository {

    private val api: TwitterApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3030")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(TwitterApi::class.java)
    }

    suspend fun createPost(content: String, userId: Int, parentId: Int? = null): Boolean {
        return try {
            val request = CreatePostRequest(userId = userId, content = content, parentId = parentId)
            val response = api.createPost(request)
            if (response.isSuccessful) {
                true
            } else {
                Log.e("PostRepository", "Error: ${response.code()} - ${response.message()}")
                false
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "Exception: ${e.message}", e)
            false
        }
    }

    suspend fun getPosts(): List<Post> {
        return try {
            val response = api.getPosts()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }


}