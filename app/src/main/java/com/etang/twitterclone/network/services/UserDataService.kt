package com.etang.twitterclone.network.services

import com.etang.twitterclone.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserDataService {

    @GET("/users/search")
    suspend fun searchUsers(@Query("query") query: String): Response<List<User>>

}