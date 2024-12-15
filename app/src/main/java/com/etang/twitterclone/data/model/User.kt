package com.etang.twitterclone.data.model

import java.io.Serializable

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val birthDate: String,
    val createdAt: String,
    val updatedAt: String?
): Serializable