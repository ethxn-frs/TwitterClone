package com.etang.twitterclone.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.etang.twitterclone.data.model.auth.LoginResponse
import com.etang.twitterclone.network.dto.auth_dto.LoginDto
import com.etang.twitterclone.network.dto.auth_dto.LoginResponseDto
import com.etang.twitterclone.network.dto.auth_dto.RegisterDto
import com.etang.twitterclone.repositories.AuthDataRepository

class LoginViewModel:ViewModel() {
    private val repository = AuthDataRepository()

    private val _loginResult = MutableLiveData<LoginResponseDto?>()
    val loginResult : LiveData<LoginResponseDto?> get() = _loginResult

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun login(email: String, password: String) {
        repository.login(email,password) { user, errorMessage ->
           if (user != null){
               _loginResult.postValue(user)
               _errorMessage.postValue(null)
           }else{
               _loginResult.postValue(null)
               _errorMessage.postValue(errorMessage)
           }
        }
    }
}