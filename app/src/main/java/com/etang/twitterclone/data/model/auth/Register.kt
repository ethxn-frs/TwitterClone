package com.etang.twitterclone.data.model.auth

data class Register(
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val birthDate: java.util.Date?
)
