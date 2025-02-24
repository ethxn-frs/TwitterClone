package com.etang.twitterclone.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etang.twitterclone.data.model.Conversation
import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.repositories.ConversationRepository
import com.etang.twitterclone.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class UserViewModel: ViewModel() {
    private val userRepository = UserRepository()

    suspend fun getAllUsersInDatabase(): List<User> = withContext(Dispatchers.IO){
        userRepository.getAllUsers()
    }

    suspend fun isFollowingUser(creatorId: Int): Boolean{
        val response: List<User>? = userRepository.getUserFollowing(creatorId)
        if (response != null) {
            return response.isNotEmpty()
        }
        return false
    }

    suspend fun getUserFollowing(creatorId: Int): List<User> = withContext(Dispatchers.IO) {
        userRepository.getUserFollowing(creatorId) ?: emptyList()
    }

    suspend fun searchUserByName(userName: String): User? = withContext(Dispatchers.IO){
        userRepository.searchUserByUsername(userName)
    }



}