package com.etang.twitterclone.network.mapper

import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.data.model.auth.Login
import com.etang.twitterclone.data.model.auth.LoginResponse
import com.etang.twitterclone.network.dto.auth_dto.LoginDto
import com.etang.twitterclone.network.dto.auth_dto.LoginResponseDto

fun mapLoginDtoToLoginModel(dto: LoginResponseDto): LoginResponse {
    return LoginResponse(
        user = dto.user,
        token = dto.token
    )
}