package com.etang.twitterclone.network.dto.auth_dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginDto(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
): Serializable
