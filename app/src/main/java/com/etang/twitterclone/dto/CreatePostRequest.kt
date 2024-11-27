package com.etang.twitterclone.dto

data class CreatePostRequest(
    val userId: Int,
    val content: String,
    val parentId: Int? = null
)