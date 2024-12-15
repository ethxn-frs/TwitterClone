package com.etang.twitterclone.data.model.auth

import com.etang.twitterclone.data.model.User
import java.io.Serializable

data class LoginResponse(
    val user : User,
    val token: String
): Serializable
