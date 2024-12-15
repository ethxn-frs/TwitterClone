package com.etang.twitterclone.network.dto.auth_dto

import com.etang.twitterclone.data.model.User
import java.io.Serializable

data class LoginResponseDto(
    val user : User,
    val token: String
): Serializable
