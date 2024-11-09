package interfaces

import Modele.User;

interface UserRepositoryInterface {
    fun createUser(user: User): User
    fun getUserById(userId: Int): User?
    fun updateUser(userId: Int, updatedUser: User): User?
    fun deleteUser(userId: Int): Boolean
    fun authenticateUser(email: String, password: String): User?
}