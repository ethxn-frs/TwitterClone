package com.etang.twitterclone.ui.main.data.model

data class User(
    val userId: Int,
    var userName: String,
    var userLastName: String,
    var userEmail: String,
    var userPhone: String,
    var userBirthDay: String,
    var userPassword: String
)