package com.etang.twitterclone.repositories

import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.network.RetrofitClient
import com.etang.twitterclone.network.dto.SearchRequestDto
import com.etang.twitterclone.network.services.UserDataService

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

}