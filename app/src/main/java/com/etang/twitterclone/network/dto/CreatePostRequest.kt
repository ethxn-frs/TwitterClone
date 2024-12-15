package com.etang.twitterclone.network.dto

data class CreatePostRequest(
    val userId: Int,
    val content: String,
    val parentId: Int? = null
)