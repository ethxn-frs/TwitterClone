package com.etang.twitterclone.network.mapper

import com.etang.twitterclone.data.model.auth.Register
import com.etang.twitterclone.data.model.auth.RegisterResponse
import com.etang.twitterclone.network.dto.auth_dto.RegisterDto
import com.etang.twitterclone.network.dto.auth_dto.RegisterResponseDto

fun mapResgiterDtoToRegisterModel(dto: RegisterResponseDto):RegisterResponse{
    return RegisterResponse(
        firstName = dto.firstName,
        lastName =  dto.lastName,
        username =  dto.username,
        email = dto.email,
        phoneNumber =  dto.phoneNumber,
        password =  dto.password,
        birthDate = dto.birthDate,
        createdAt = dto.createdAt,
        updatedAt = dto.updatedAt,
        followers = dto.followers,
        following = dto.following,
        posts = dto.posts,
        likedPosts = dto.likedPosts,
        conversations = dto.conversations,
        messages = dto.messages
    )
}