package com.etang.twitterclone.data.model

data class SettingItem(
    val icon: Int,
    val title: String,
    val description: String,
    val action: () -> Unit
)
