package com.etang.twitterclone.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.etang.twitterclone.network.dto.UpdateUserDto
import com.etang.twitterclone.network.dto.auth_dto.LoginResponseDto
import com.etang.twitterclone.repositories.AuthDataRepository

class UpdateUserViewModel: ViewModel() {
    private val repository = AuthDataRepository()

    private val _updateResult = MutableLiveData<UpdateUserDto?>()
    val updateResult : LiveData<UpdateUserDto?> get() = _updateResult

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun updateUserData(updateUserData: UpdateUserDto) {
        repository.updateUserData(updateUserData) { user, errorMessage ->
            if (user != null){
                _updateResult.postValue(user)
                _errorMessage.postValue(null)
            }else{
                _updateResult.postValue(null)
                _errorMessage.postValue(errorMessage)
            }
        }
    }
}