package com.etang.twitterclone.repositories

import android.util.Log
import com.etang.twitterclone.data.model.Post
import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.network.RetrofitClient
import com.etang.twitterclone.network.dto.SearchRequestDto
import com.etang.twitterclone.network.services.UserDataService
import retrofit2.Response

class UserRepository {

    private val userDataService = RetrofitClient.instance.create(UserDataService::class.java)


    suspend fun searchUsers(query: String): List<User> {
        return try {
            val request = SearchRequestDto(query)
            val response = userDataService.searchUsers(request)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getUserPosts(userId: Int): List<Post> {
        return try {
            val response: Response<List<Post>> = userDataService.getUserPosts(userId)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e("UserRepository", "Error: ${response.code()} - ${response.message()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Exception: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun getUserComments(userId: Int): List<Post> {
        return try {
            val response: Response<List<Post>> = userDataService.getUserComments(userId)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e("UserRepository", "Error: ${response.code()} - ${response.message()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Exception: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun getUserLikedPosts(userId: Int): List<Post> {
        return try {
            val response: Response<List<Post>> = userDataService.getUserLikedPosts(userId)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e("UserRepository", "Error: ${response.code()} - ${response.message()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Exception: ${e.message}", e)
            emptyList()
        }
    }

}