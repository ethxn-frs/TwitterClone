package com.etang.twitterclone.network.services

import com.etang.twitterclone.network.dto.auth_dto.LoginDto
import com.etang.twitterclone.network.dto.auth_dto.LoginResponseDto
import com.etang.twitterclone.network.dto.auth_dto.RegisterDto
import com.etang.twitterclone.network.dto.auth_dto.RegisterResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthDataService {
    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(@Body request: LoginDto): Call<LoginResponseDto>

    @POST("change-password")
    fun changePassword(@Body request: Map<String, String>)

    @POST("lost-password")
    fun forgotPassword(@Body request: Map<String, String>)

    @Headers("Content-Type: application/json")
    @POST("signup")
    fun signup(@Body request: RegisterDto): Call<RegisterResponseDto>
}