package com.etang.twitterclone.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.etang.twitterclone.network.dto.auth_dto.RegisterDto
import com.etang.twitterclone.network.dto.auth_dto.RegisterResponseDto
import com.etang.twitterclone.repositories.AuthDataRepository

class RegisterViewModel():ViewModel() {

    private val repository = AuthDataRepository()

    private val _registrationSuccess = MutableLiveData<RegisterResponseDto?>()
    val registrationSuccess : LiveData<RegisterResponseDto?> get() = _registrationSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun registerUser(registerData: RegisterDto) {
        Log.d("RegisterDto","RegisterDto est : $registerData")
        repository.signup(registerData) { user, errorMessage ->
            if (user != null){
                _registrationSuccess.postValue(user)
                _errorMessage.postValue(null)
            }else{
                _registrationSuccess.postValue(null)
                _errorMessage.postValue(errorMessage)
            }
        }
    }
}