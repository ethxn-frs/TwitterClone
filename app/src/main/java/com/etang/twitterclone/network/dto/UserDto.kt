package com.etang.twitterclone.network.dto

data class UserDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val birthDate: String,
    val createdAt: String,
    val updatedAt: String?
)
