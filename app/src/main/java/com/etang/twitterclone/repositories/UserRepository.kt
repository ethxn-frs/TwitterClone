package com.etang.twitterclone.repositories

import android.util.Log
import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.network.RetrofitClient
import com.etang.twitterclone.network.services.UserDataSevice
import com.etang.twitterclone.network.services.UsernameSearchRequest
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import retrofit2.Response

class UserRepository {
    private val service: UserDataSevice = RetrofitClient.instance.create(UserDataSevice::class.java)

    suspend fun searchUserByUsername(username: String): User? {
        val request = UsernameSearchRequest(username)
        val response: Response<List<User>> = service.searchUser(request)
        Log.d("UserRepository", "Response code: ${response.code()}, body: ${response.body()}")
        return if (response.isSuccessful){
            val users = response.body()
            if(!users.isNullOrEmpty()) users[0] else null
        }
        else{
            null
        }
    }

    suspend fun isFollowingUser(creatorId: Int, targetId: Int): Boolean{
        val response: Response<List<User>> = service.getUserFollowing(creatorId)
        return if(response.isSuccessful){
            val followingList = response.body() ?: emptyList()
            followingList.any{ it.id == targetId}
        }else{
            false
        }
    }

    suspend fun getUserFollowing(creatorId: Int): List<User>? {
        val response: Response<List<User>> = service.getUserFollowing(creatorId)
        return if(response.isSuccessful){
            response.body()
        }else{
            null
        }

    }

    suspend fun getAllUsers(): List<User>{
        val response: Response<JsonElement> = service.getAllUsers()
        if(response.isSuccessful){
            val jsonElement = response.body()
            if(jsonElement != null && jsonElement.isJsonArray){
                val jsonArray = jsonElement.asJsonArray
                if(jsonArray.size() > 0){
                    val usersJson = jsonArray[0]
                    return Gson().fromJson(usersJson, object : TypeToken<List<User>>() {}.type)

                }
            }
            return emptyList()
        }else{
            throw Exception("Failed to fetch all users")
        }
    }

    suspend fun isUserFollowingCreator(creatorId: Int, userId: Int): Boolean {
        val response: Response<List<User>> = service.getUserFollowers(userId)
        return if(response.isSuccessful){
            val followers = response.body() ?: emptyList()
            followers.any{it.id == creatorId}
        }else{
            false
        }
    }

}