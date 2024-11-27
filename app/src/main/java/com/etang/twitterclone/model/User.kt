package com.etang.twitterclone.model

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val birthDate: String,
    val followersCount: Int = 0,
    val followingCount: Int = 0
)