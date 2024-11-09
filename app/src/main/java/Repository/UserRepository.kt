package Repository
import interfaces.UserRepositoryInterface;
import Modele.User

class UserRepository : UserRepositoryInterface{
    private val users = mutableListOf<User>()

    override fun createUser(user: User): User {
        users.add(user)
        return user
    }

    override fun getUserById(userId: Int): User? {
        return users.find { it.userId == userId }
    }

    override fun deleteUser(userId: Int): Boolean {
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
}