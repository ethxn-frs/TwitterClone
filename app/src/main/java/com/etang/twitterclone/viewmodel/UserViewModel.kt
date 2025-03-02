package com.etang.twitterclone.viewmodel

import androidx.lifecycle.ViewModel
import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()

    suspend fun getAllUsersInDatabase(): List<User> = withContext(Dispatchers.IO) {
        userRepository.getAllUsers()
    }

    suspend fun isFollowingUser(creatorId: Int): Boolean {
        val response: List<User>? = userRepository.getUserFollowing(creatorId)
        if (response != null) {
            return response.isNotEmpty()
        }
        return false
    }

    suspend fun getUserFollowing(creatorId: Int): List<User> = withContext(Dispatchers.IO) {
        userRepository.getUserFollowing(creatorId) ?: emptyList()
    }

    suspend fun searchUserByName(userName: String): User? = withContext(Dispatchers.IO) {
        userRepository.searchUserByUsername(userName)
    }


}
