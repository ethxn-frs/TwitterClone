package com.etang.twitterclone.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.etang.twitterclone.data.model.auth.Login
import com.etang.twitterclone.data.model.auth.LoginResponse
import com.etang.twitterclone.data.model.auth.Register
import com.etang.twitterclone.data.model.auth.RegisterResponse
import com.etang.twitterclone.network.RetrofitClient
import com.etang.twitterclone.network.dto.UpdateUserDto
import com.etang.twitterclone.network.dto.auth_dto.LoginDto
import com.etang.twitterclone.network.dto.auth_dto.LoginResponseDto
import com.etang.twitterclone.network.dto.auth_dto.RegisterDto
import com.etang.twitterclone.network.dto.auth_dto.RegisterResponseDto
import com.etang.twitterclone.network.mapper.mapLoginDtoToLoginModel
import com.etang.twitterclone.network.mapper.mapResgiterDtoToRegisterModel
import com.etang.twitterclone.network.services.AuthDataService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthDataRepository {
    private val authDataService = RetrofitClient.instance.create(AuthDataService::class.java)
    private val _loginResponse = MutableLiveData<LoginResponse>()
    private val _signupResponse = MutableLiveData<RegisterResponse>()

    val loginResponse : LiveData<LoginResponse> get() = _loginResponse
    val signupResponse : LiveData<RegisterResponse> get() = _signupResponse

    fun login(email :String, password:String, callback: (LoginResponseDto?, String?) -> Unit){
        val loginRequest = LoginDto(email,password)
        val call = authDataService.login(loginRequest)
        Log.d("AuthDataRepository", "Corps de la requête : $loginRequest")

        call.enqueue(object :Callback<LoginResponseDto>{
            override fun onResponse(call: Call<LoginResponseDto>, response: Response<LoginResponseDto>) {
                Log.d("AuthDataRepository", "LoginResponseDto is: $response")
                if(response.isSuccessful){

                    val responseBody = response.body()

                    if(responseBody !=null){
                        _loginResponse.value = responseBody?.let {
                            mapLoginDtoToLoginModel(it)
                        }
                        callback(responseBody, null)
                        Log.d("AuthDataRepository", "Connexion réussie")
                    }
                    else{
                        callback(null, "Réponse vide du serveur")
                    }
                }else{
                    Log.d("AuthDataRepository", "Échec de la connexion : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LoginResponseDto>, t: Throwable) {
                Log.d("AuthDataRepository", "Erreur réseau : ${t.message}")
            }
        })
    }

    fun signup(registerData: RegisterDto,callback: (RegisterResponseDto?, String?) -> Unit){

        val call = authDataService.signup(registerData)

        call.enqueue(object : Callback<RegisterResponseDto>{
            override fun onResponse(call: Call<RegisterResponseDto>, response: Response<RegisterResponseDto>) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null ){
                        responseBody?.let {
                            mapResgiterDtoToRegisterModel(it)
                        }
                        callback(responseBody, null)
                        Log.d("AuthDataRepository", "Création de compte réussie")
                    }else{
                        callback(null, "Réponse vide du serveur")
                    }

                }else{
                    Log.d("AuthDataRepository","Erreur : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponseDto>, t: Throwable) {
                Log.d("AuthDataRepository","Erreur : ${t.message}")
            }
        })

    }

    fun updateUserData(updateUserData: UpdateUserDto, callback: (UpdateUserDto?, String?) -> Unit){

        val call = authDataService.updateUserData(updateUserData)
        call.enqueue(object : Callback<UpdateUserDto>{
            override fun onResponse(call: Call<UpdateUserDto>, response: Response<UpdateUserDto>) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null ){
                        callback(responseBody, null)
                        Log.d("AuthDataRepository", "Update réussie")
                    }else{
                        callback(null, "Réponse vide du serveur")
                    }

                }else{
                    Log.d("AuthDataRepository","Erreur : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UpdateUserDto>, t: Throwable) {
                Log.d("AuthDataRepository","Erreur : ${t.message}")
            }
        })
    }

}