package com.etang.twitterclone.session

import android.content.Context
import android.content.SharedPreferences
import com.etang.twitterclone.data.model.User
import com.google.gson.Gson

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()
    private val gson = Gson()

    fun saveUserSession(token: String, user: User) {
        val userJson = gson.toJson(user)
        editor.putString("TOKEN", token)
        editor.putString("USER", userJson)
        editor.apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("TOKEN", null)
    }

    fun getUser(): User? {
        val userJson = sharedPreferences.getString("USER", null)
        return if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

    fun getUserId(): Int {
        val userJson = sharedPreferences.getString("USER", null)
        return gson.fromJson(userJson, User::class.java).id

    }

    fun logout() {
        val editor = sharedPreferences.edit()
        editor.clear() // Supprime toutes les données stockées
        editor.apply()
    }

    fun clearSession() {
        editor.clear()
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.contains("TOKEN")
    }
}