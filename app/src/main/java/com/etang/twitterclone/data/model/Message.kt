package com.etang.twitterclone.data.model

import java.sql.Date

data class Message(
    val id: Int,
    val author: User,
    val sentAt: Date,
    val content: String,
    val conversation: Conversation,
    val seenBy: User
)