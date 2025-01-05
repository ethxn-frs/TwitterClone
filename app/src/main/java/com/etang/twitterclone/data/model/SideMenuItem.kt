package com.etang.twitterclone.data.model

data class SideMenuItem(
    val icon: Int,
    val title: String,
    val action: () -> Unit
    )
