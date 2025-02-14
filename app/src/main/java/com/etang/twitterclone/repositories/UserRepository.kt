package com.etang.twitterclone.repositories

import android.util.Log
import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.network.RetrofitClient
import com.etang.twitterclone.network.services.UserDataSevice
import com.etang.twitterclone.network.services.UsernameSearchRequest
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

    suspend fun isFollowingUser(followerId:Int, followingId: Int): Boolean{
        val response: Response<List<User>> = service.getUserFollowing(followerId)
        return if(response.isSuccessful){
            true
        }else{
            false
        }
    }

}