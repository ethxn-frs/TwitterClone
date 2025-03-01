package com.etang.twitterclone.data.model

import java.io.Serializable

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val bio: String?,
    val location: String?,
    val website: String?,
    val coverPictureUrl: String?,
    val profilePictureUrl: String?,
    val birthDate: String,
    val createdAt: String,
    val updatedAt: String?,
    val followers: MutableList<User>,
    val following: MutableList<User>,
) : Serializable