package com.etang.twitterclone.ui.main.data.repository

import android.util.Patterns
import com.etang.twitterclone.ui.main.data.model.User
import com.etang.twitterclone.ui.main.interfaces.UserRepositoryInterface

class userRepository: UserRepositoryInterface {
    private val users = mutableListOf<User>()
    private var currentUser: User? = null

    override fun login(email: String, password: String):Boolean {
        if (!isEmailValid(email)) {
            throw IllegalArgumentException("Email invalide")
        }

        val user = users.find { it.userEmail == email && it.userPassword == password }

        return if (user !=null){
            currentUser = user
            true
        }else{
            false
        }
    }

    override fun logout() {
        currentUser = null
    }

    override fun createUser(user: User): User {
        if (!isEmailValid(user.userEmail)) {
            throw IllegalArgumentException("Email invalide")
        }

        if (users.any { it.userEmail == user.userEmail }) {
            throw IllegalArgumentException("Un utilisateur avec cet email existe déjà")
        }
        users.add(user)
        return user
    }

    override fun getUserById(userId: Int): User? {
        return users.find { it.userId == userId }
    }

    override fun deleteUser(userId: Int): Boolean {
        val userExists = users.any { it.userId == userId }

        if (!userExists) {
            throw IllegalArgumentException("Utilisateur introuvable avec l'ID : $userId")
        }
        return users.removeIf { it.userId == userId }
    }

    override fun authenticateUser(email: String, password: String): User? {
        return users.find { it.userEmail == email && it.userPassword == password }
    }

    override fun updateUser(userId: Int, updatedUser: User): User? {
        val userIndex = users.indexOfFirst { it.userId == userId }
        return if (userIndex != -1) {
            users[userIndex] = updatedUser
            updatedUser
        } else {
            null
        }
    }
    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}