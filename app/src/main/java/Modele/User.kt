package Modele

data class User(
    val userId: Int,
    var userName: String,
    var userLastName: String,
    var userEmail: String,
    var userPhone: String,
    var userBirthDay: String,
    var userPassword: String
){
    fun login(password: String): Boolean {
        return this.userPassword == password
    }

    fun logout() {
    }

    fun sendMessage(recipientId: Int, message: String) {
    }

    fun createPost(content: String) {
    }

    fun forgotPassword() {
    }

    fun resetPassword(newPassword: String) {
        this.userPassword = newPassword
    }

    fun createComment(postId: Int, content: String) {
    }

    fun likeComment(commentId: Int) {
    }

    fun deletePost(postId: Int) {
    }

}
