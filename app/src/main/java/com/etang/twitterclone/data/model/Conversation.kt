package com.etang.twitterclone.data.model

import java.sql.Date

data class Conversation(
    val id: Int,
    val name: String,
    val createdAt: Date,
    val users: User,
    val messages: Message,
    val profileImage: String?
)
