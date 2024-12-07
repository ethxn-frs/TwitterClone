package com.etang.twitterclone.ui.main.data.remote
import com.etang.twitterclone.ui.main.data.model.User
import retrofit2.http.GET


interface UserApiService {
    // Récupérer tous les utilisateurs
    @GET("users")
    fun getUsers(): Call<List<User>>

    // Récupérer un utilisateur par ID
    @GET("users/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>

    // Créer un nouvel utilisateur
    @POST("users")
    fun createUser(@Body user: User): Call<User>

    // Mettre à jour un utilisateur existant
    @PUT("users/{id}")
    fun updateUser(@Path("id") id: Int, @Body user: User): Call<User>

    // Supprimer un utilisateur par ID
    @DELETE("users/{id}")
    fun deleteUser(@Path("id") id: Int): Call<Void>

    // Authentifier un utilisateur (connexion)
    @POST("users/login")
    fun login(@Body credentials: Map<String, String>): Call<User>

    // Déconnecter un utilisateur (optionnel, si nécessaire)
    @POST("users/logout")
    fun logout(@Header("Authorization") token: String): Call<Void>

    // Vérifier l'email d'un utilisateur (fonction de validation)
    @GET("users/validate-email")
    fun validateEmail(@Query("email") email: String): Call<Boolean>

    // Réinitialiser le mot de passe d'un utilisateur
    @POST("users/reset-password")
    fun resetPassword(@Body request: Map<String, String>): Call<Void>

    // Ajouter un "suivi" (follow) entre utilisateurs
    @POST("users/{id}/follow")
    fun followUser(@Path("id") userId: Int, @Header("Authorization") token: String): Call<Void>

    // Supprimer un "suivi" (unfollow) entre utilisateurs
    @DELETE("users/{id}/follow")
    fun unfollowUser(@Path("id") userId: Int, @Header("Authorization") token: String): Call<Void>

    // Récupérer la liste des utilisateurs suivis par un utilisateur
    @GET("users/{id}/following")
    fun getFollowing(@Path("id") userId: Int): Call<List<User>>

    // Récupérer la liste des abonnés d'un utilisateur
    @GET("users/{id}/followers")
    fun getFollowers(@Path("id") userId: Int): Call<List<User>>
}
