package com.etang.twitterclone.repositories

import android.util.Log
import com.etang.twitterclone.data.model.Post
import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.network.RetrofitClient
import com.etang.twitterclone.network.dto.SearchRequestDto
import com.etang.twitterclone.network.services.UserDataService
import com.etang.twitterclone.network.services.UsernameSearchRequest
import com.google.gson.Gson
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

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

    fun deleteCoverPicture(userId: Int) {
        try {
            userDataService.deleteCoverPicture(userId);
        } catch (e: Exception) {
            Log.e("UserRepository", "Exception: ${e.message}", e)
        }
    }

    fun deleteProfilePicture(userId: Int) {
        try {
            userDataService.deleteProfilePicture(userId);
        } catch (e: Exception) {
            Log.e("UserRepository", "Exception: ${e.message}", e)
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

    suspend fun getUserById(userId: Int): User? {
        return try {
            val response = userDataService.getUserById(userId)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun followUser(followerId: Int, followeeId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val response = userDataService.followUser(
                mapOf(
                    "followerId" to followerId,
                    "followeeId" to followeeId
                )
            )
            response.isSuccessful
        }
    }

    suspend fun unfollowUser(followerId: Int, followeeId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val response = userDataService.unfollowUser(
                mapOf(
                    "followerId" to followerId,
                    "followeeId" to followeeId
                )
            )
            response.isSuccessful
        }
    }

    suspend fun isFollowing(followerId: Int, followeeId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val response = userDataService.isFollowing(followerId, followeeId)
            if (response.isSuccessful) {
                response.body()?.isFollowing ?: false
            } else {
                false
            }
        }
    }

    suspend fun updateUserField(userId: Int, field: String, value: String): Boolean {
        return try {
            val response = userDataService.updateUserField(userId, mapOf(field to value))
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getFollowers(userId: Int): List<User> {
        return try {
            val response = userDataService.getUserFollowers(userId)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getFollowings(userId: Int): List<User> {
        return try {
            val response = userDataService.getUserFollowings(userId)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateProfileImage(
        userId: Int,
        file: File,
        type: String,
    ): Boolean {
        return try {
            val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val response =
                if (type == "pp") {
                    userDataService.updateProfilePicture(userId, body)
                } else {
                    userDataService.updateCoverPicture(userId, body)
                }
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun searchUserByUsername(username: String): User? {
        val request = UsernameSearchRequest(username)
        val response: Response<List<User>> = userDataService.searchUser(request)
        Log.d("UserRepository", "Response code: ${response.code()}, body: ${response.body()}")
        return if (response.isSuccessful) {
            val users = response.body()
            if (!users.isNullOrEmpty()) users[0] else null
        } else {
            null
        }
    }

    suspend fun isFollowingUser(creatorId: Int, targetId: Int): Boolean {
        val response: Response<List<User>> = userDataService.getUserFollowing(creatorId)
        return if (response.isSuccessful) {
            val followingList = response.body() ?: emptyList()
            followingList.any { it.id == targetId }
        } else {
            false
        }
    }

    suspend fun getUserFollowing(creatorId: Int): List<User>? {
        val response: Response<List<User>> = userDataService.getUserFollowing(creatorId)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }

    }

    suspend fun getAllUsers(): List<User> {
        val response: Response<JsonElement> = userDataService.getAllUsers()
        if (response.isSuccessful) {
            val jsonElement = response.body()
            if (jsonElement != null && jsonElement.isJsonArray) {
                val jsonArray = jsonElement.asJsonArray
                if (jsonArray.size() > 0) {
                    val usersJson = jsonArray[0]
                    return Gson().fromJson(
                        usersJson,
                        object : com.google.gson.reflect.TypeToken<List<User>>() {}.type
                    )

                }
            }
            return emptyList()
        } else {
            throw Exception("Failed to fetch all users")
        }
    }

    suspend fun isUserFollowingCreator(creatorId: Int, userId: Int): Boolean {
        val response: Response<List<User>> = userDataService.getUserFollowers(creatorId)
        return if (response.isSuccessful) {
            val followers = response.body() ?: emptyList()
            followers.any { it.id == userId }
        } else {
            false
        }
    }

}