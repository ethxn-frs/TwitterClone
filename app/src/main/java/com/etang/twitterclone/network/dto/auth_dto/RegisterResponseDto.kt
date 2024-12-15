package com.etang.twitterclone.network.dto.auth_dto

import com.etang.twitterclone.data.model.Conversation
import com.etang.twitterclone.data.model.Message
import com.etang.twitterclone.data.model.Post
import java.io.Serializable

data class RegisterResponseDto(
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val birthDate: java.util.Date?,
    val createdAt: java.util.Date,
    val updatedAt : java.util.Date?,
    val followers: List<Int>,
    val following: List<Int>,
    val posts: List<Post>,
    val likedPosts: List<Int>,
    val conversations: List<Conversation>,
    val messages: List<Message>
): Serializable
