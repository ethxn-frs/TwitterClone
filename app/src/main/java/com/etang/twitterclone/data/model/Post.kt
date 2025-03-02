package com.etang.twitterclone.data.model

import com.google.gson.annotations.SerializedName

data class Post(
    val id: Int,
    @SerializedName("author") val author: User,
    @SerializedName("content") val content: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("deleted") val deleted: Boolean = false,
    @SerializedName("parentPost") val parentPost: Post? = null,
    @SerializedName("comments") val comments: List<Post> = emptyList(),
    @SerializedName("userHaveLiked") var userHaveLiked: List<User> = emptyList()
)
