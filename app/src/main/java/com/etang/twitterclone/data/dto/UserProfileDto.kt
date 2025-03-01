package com.etang.twitterclone.data.dto

import com.etang.twitterclone.data.model.User

data class UserProfileDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val bio: String? = null,
    val location: String? = null,
    val website: String? = null,
    val birthDate: String?,
    val createdAt: String,
    val coverPictureUrl: String? = null,
    val profilePictureUrl: String? = null,
    val followersCount: Int = 0,
    val followingCount: Int = 0
) {
    constructor(user: User) : this(
        id = user.id,
        firstName = user.firstName,
        lastName = user.lastName,
        username = user.username,
        birthDate = user.birthDate,
        createdAt = user.createdAt,
        bio = user.bio,
        location = user.location,
        website = user.website,
        coverPictureUrl = user.coverPictureUrl,
        profilePictureUrl = user.profilePictureUrl,
        followersCount = user.followers.size,
        followingCount = user.following.size
    )
}