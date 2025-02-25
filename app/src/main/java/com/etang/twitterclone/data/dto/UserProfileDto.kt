package com.etang.twitterclone.data.dto

data class UserProfileDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val bio: String?,
    val location: String?,
    val website: String?,
    val birthDate: String?,
    val createdAt: String,
    val coverPictureUrl: String?,
    val profilePictureUrl: String?,
    val followersCount: Int,
    val followingCount: Int
)
