package com.etang.twitterclone.data.model

data class Post(
    val id: Int,
    val author: User,
    val content: String,
    val createdAt: String,
    val deleted: Boolean = false,
    val parentPost: Post? = null,
    val comments: List<Post> = emptyList(),
    val userHaveLiked: List<User> = emptyList()
)
