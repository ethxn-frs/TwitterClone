package com.etang.twitterclone.data.model

data class Message(
    val id: Int,
    val sentAt: String,
    val content: String,
    val author: User,
    val seenBy: List<User>,
    var reaction: String?
)