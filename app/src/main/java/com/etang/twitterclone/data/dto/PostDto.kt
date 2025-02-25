package com.etang.twitterclone.data.dto

data class PostDto(
    val id: Int,
    val content: String,
    val createdAt: String,
    val authorUsername: String,
    val likesCount: Int,
    val commentsCount: Int
)