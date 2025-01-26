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
    private  var tvProfileName: TextView? = null
    private  var tvProfileUsername: TextView? = null
    private  var ivProfileImage: ImageView? = null
    private  var personnalInfo: CardView? = null
    private  var parameter: CardView? = null
    //private lateinit var backToHomePage: ImageView
    private  var switchNotifications: Switch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)
        val user = sessionManager.getUser()

        try {
            initializeViews()
            initialyseUserData(user)
            //setupClickListeners()
        } catch (e: Exception) {
            Log.e("ProfileActivity", "Erreur d'initialisation des vues : ${e.message}")
            Toast.makeText(this, "Une erreur s'est produite", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializeViews() {
        try {
            tvProfileName = findViewById(R.id.tvProfileName)
            tvProfileUsername = findViewById(R.id.tvProfileUsername)
            ivProfileImage = findViewById(R.id.ivProfileImage)
            personnalInfo = findViewById(R.id.cardPersonalInfo)
            parameter = findViewById(R.id.cardViewSettings)
            switchNotifications = parameter?.findViewById(R.id.switchNotifications)

            ivProfileImage?.setImageResource(R.drawable.ic_profile) // Chargement d'une image par défaut
        } catch (e: Exception) {
            Log.e("ProfileActivity", "Erreur d'initialisation des vues : ${e.message}")
            throw e
        }
    }

    private fun setupClickListeners() {
        // Modifier la photo de profil
        findViewById<TextView>(R.id.tvChangeProfile).setOnClickListener {
            Toast.makeText(this, "Modifier la photo de profil", Toast.LENGTH_SHORT).show()
            // Implémentez l'ouverture d'une page pour modifier la photo
        }

        // Modifier les informations personnelles
        findViewById<ImageView>(R.id.editeProfil).setOnClickListener {
            navigateToEditProfile()
        }

        // Notifications
        switchNotifications?.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Notifications activées" else "Notifications désactivées"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        // Supprimer le compte
        parameter?.findViewById<TextView>(R.id.deleteCompte)?.setOnClickListener {
            Toast.makeText(this, "Implémentez la suppression du compte", Toast.LENGTH_SHORT).show()
        }

        // Déconnexion
        parameter?.findViewById<TextView>(R.id.logout)?.setOnClickListener {
            sessionManager.clearSession()
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }

        // Retour à la page d'accueil
        /*backToHomePage.setOnClickListener {
            navigateToHomePage()
        }*/
    }

    private fun navigateToEditProfile() {
        val intent = Intent(this, EditProfileActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToHomePage() {
        val intent = Intent(this, TimelineActivity::class.java)
        startActivity(intent)
    }

    fun initialyseUserData(user: User?){
        //val tvProfileName = findViewById<TextView>(R.id.tvProfileName)
        //val tvProfileUsername = findViewById<TextView>(R.id.tvProfileUsername)
        if (user != null){
            tvProfileName?.text ="${user.firstName}  ${user.lastName}"
            tvProfileUsername?.text = "@${user.username}"

            personnalInfo?.findViewById<TextView>(R.id.name)?.text =user.firstName
            personnalInfo?.findViewById<TextView>(R.id.lastname)?.text =user.lastName
            personnalInfo?.findViewById<TextView>(R.id.phone_number)?.text = user.phoneNumber
            personnalInfo?.findViewById<TextView>(R.id.email)?.text =user.email
        }
    }

    fun editePersonalData(){
        findViewById<ImageView>(R.id.editeProfil).setOnClickListener {
            Toast.makeText(this, "implémanter page de modification", Toast.LENGTH_SHORT).show()
        }
    }
}