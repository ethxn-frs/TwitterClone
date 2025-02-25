package com.etang.twitterclone.network.services

import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.network.dto.SearchRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserDataService {

    @POST("/users/search")
    suspend fun searchUsers(@Body request: SearchRequestDto): Response<List<User>>

}