package com.etang.twitterclone.pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.etang.twitterclone.R
import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.pages.post.TimelineActivity
import com.etang.twitterclone.session.SessionManager

class ProfileActivity:AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var headerlayout : androidx.constraintlayout.widget.ConstraintLayout
    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileUsername: TextView
    private lateinit var ivProfileImage: ImageView
    private lateinit var tvChangeProfile: TextView
    private lateinit var personnalInfo: CardView
    private lateinit var parameter: CardView
    private lateinit var backToHomePage: ImageView
    private lateinit var switchNotifications: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        sessionManager = SessionManager(this)
        initializeViews()

        val user = sessionManager.getUser()
        if (user != null) {
            populateUserData(user)
        } else {
            Log.e("ProfileActivity", "User data is null")
            Toast.makeText(this, "Erreur : utilisateur introuvable", Toast.LENGTH_SHORT).show()
        }

        setupClickListeners()
    }


    /** Initialise toutes les vues de l'activité */
    private fun initializeViews() {
        tvProfileName = findViewById(R.id.tvProfileName)
        headerlayout = findViewById(R.id.headerProfile)
        tvProfileUsername = findViewById(R.id.tvProfileUsername)
        ivProfileImage = findViewById(R.id.ivProfileImage)
        tvChangeProfile = findViewById(R.id.tvChangeProfile)
        personnalInfo = findViewById(R.id.cardPersonalInfo)
        parameter = findViewById(R.id.cardViewSettings)
        backToHomePage = headerlayout.findViewById(R.id.ivBack)
        switchNotifications = parameter.findViewById(R.id.switchNotifications)

        ivProfileImage.setImageResource(R.drawable.ic_profile)
    }

    /** Remplit les informations de l'utilisateur dans les vues */
    private fun populateUserData(user: User) {
        tvProfileName.text = "${user.firstName} ${user.lastName}"
        tvProfileUsername.text = "@${user.username}"

        personnalInfo.findViewById<TextView>(R.id.name).text = user.firstName
        personnalInfo.findViewById<TextView>(R.id.lastname).text = user.lastName
        personnalInfo.findViewById<TextView>(R.id.phone_number).text = user.phoneNumber
        personnalInfo.findViewById<TextView>(R.id.email).text = user.email
    }

    /** Définit les actions des différents boutons */
    private fun setupClickListeners() {
        // Modifier la photo de profil
        tvChangeProfile.setOnClickListener {
            Toast.makeText(this, "Modifier la photo de profil", Toast.LENGTH_SHORT).show()
            // Ajouter la logique pour modifier la photo
        }

        // Modifier les informations personnelles
        findViewById<ImageView>(R.id.editeProfil).setOnClickListener {
            Toast.makeText(this, "Implémentez la page de modification", Toast.LENGTH_SHORT).show()
            // Navigation vers la page de modification (à implémenter)
        }

        // Gestion des notifications
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Notifications activées" else "Notifications désactivées"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        // Suppression du compte
        parameter.findViewById<TextView>(R.id.deleteCompte).setOnClickListener {
            Toast.makeText(this, "Implémentez la suppression du compte", Toast.LENGTH_SHORT).show()
        }

        // Déconnexion
        parameter.findViewById<TextView>(R.id.logout).setOnClickListener {
            sessionManager.clearSession()
            navigateToWelcomeScreen()
        }

        // Retour à la page d'accueil
        backToHomePage.setOnClickListener {
            navigateToHomePage()
        }
    }

    /** Navigation vers l'écran d'accueil */
    private fun navigateToHomePage() {
        val intent = Intent(this, TimelineActivity::class.java)
        startActivity(intent)
    }

    /** Navigation vers l'écran de bienvenue */
    private fun navigateToWelcomeScreen() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToEditeUserInformations(){

    }
}