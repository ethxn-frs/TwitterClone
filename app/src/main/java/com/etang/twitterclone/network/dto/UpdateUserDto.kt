package com.etang.twitterclone.network.dto

data class UpdateUserDto(
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val phoneNumber: String,
    )
