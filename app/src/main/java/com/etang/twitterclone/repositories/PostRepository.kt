package com.etang.twitterclone.repositories

import android.util.Log
import com.etang.twitterclone.data.model.Post
import com.etang.twitterclone.network.RetrofitClient
import com.etang.twitterclone.network.dto.CreatePostRequest
import com.etang.twitterclone.network.services.PostDataService

class PostRepository {

    private val postDataService = RetrofitClient.instance.create(PostDataService::class.java)

    suspend fun createPost(content: String, userId: Int, parentId: Int? = null): Boolean {
        return try {
            val request = CreatePostRequest(userId = userId, content = content, parentId = parentId)
            val response = postDataService.createPost(request)
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
            val response = postDataService.getPosts()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun likePost(postId: Int, userId: Int): Boolean {
        return try {
            val response = postDataService.likePost(postId, mapOf("userId" to userId))
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

    suspend fun getPostById(postId: Int): Post? {
        return try {
            val response = postDataService.getPostById(postId)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("PostRepository", "Error: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "Exception: ${e.message}", e)
            null
        }
    }

}