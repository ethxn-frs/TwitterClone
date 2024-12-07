package com.etang.twitterclone.ui.main.interfaces

import com.etang.twitterclone.ui.main.data.model.User


interface UserRepositoryInterface {
    fun login(email: String,password: String):Boolean
    fun logout()
    fun createUser(user: User): User
    fun getUserById(userId: Int): User?
    fun updateUser(userId: Int, updatedUser: User): User?
    fun deleteUser(userId: Int): Boolean
    fun authenticateUser(email: String, password: String): User?
}