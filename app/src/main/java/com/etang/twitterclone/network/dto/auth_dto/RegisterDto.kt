package com.etang.twitterclone.network.dto.auth_dto

import com.google.gson.annotations.SerializedName
import java.util.Date

data class RegisterDto(
    @SerializedName("firstname")
    val firstname: String,

    @SerializedName("lastname")
    val lastname: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("phoneNumber")
    val phoneNumber: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("birthDate")
    val birthDate: Date?
)
