package com.etang.twitterclone.data.model

data class Conversation(
    val id: Int,
    val name: String,
    val createdAt: String,
    val users: List<User>,
    val messages: List<Message>,
)