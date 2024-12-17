package com.etang.twitterclone.pages

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.etang.twitterclone.R
import com.etang.twitterclone.pages.post.TimelineActivity
import com.etang.twitterclone.session.SessionManager

class ProfileActivity:AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        sessionManager = SessionManager(this)

        val tvProfileName = findViewById<TextView>(R.id.tvProfileName)
        val tvProfileUsername = findViewById<TextView>(R.id.tvProfileUsername)
        val ivProfileImage = findViewById<ImageView>(R.id.ivProfileImage)
        val tvChangeProfile = findViewById<TextView>(R.id.tvChangeProfile)

        val headerlayout = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.headerProfile)
        val personnalInfo = findViewById<androidx.cardview.widget.CardView>(R.id.cardPersonalInfo)
        val parameter = findViewById<androidx.cardview.widget.CardView>(R.id.cardViewSettings)

        val backToHomePage = headerlayout.findViewById<ImageView>(R.id.ivBack)

        // variable pour la gestion des paramètres
        val switchNotifications = parameter.findViewById<Switch>(R.id.switchNotifications)
        //val deleteAccount = parameter.findViewById<TextView>(R.id.deleteCompte)
        //val logout = parameter.findViewById<TextView>(R.id.logout)


        ivProfileImage.setImageResource(R.drawable.ic_profile)

        var user = sessionManager.getUser()

        /*------------------------------------ Informations personnelles ---------------------------------------------*/

        if (user != null){
            tvProfileName.text ="${user.firstName}  ${user.lastName}"
            tvProfileUsername.text = "@${user.username}"

            personnalInfo.findViewById<TextView>(R.id.name).text =user.firstName
            personnalInfo.findViewById<TextView>(R.id.lastname).text =user.lastName
            personnalInfo.findViewById<TextView>(R.id.phone_number).text = user.phoneNumber
            personnalInfo.findViewById<TextView>(R.id.email).text =user.email
        }

        findViewById<ImageView>(R.id.editeProfil).setOnClickListener {
            Toast.makeText(this, "implémanter page de modification", Toast.LENGTH_SHORT).show()
        }

        tvChangeProfile.setOnClickListener {
            Toast.makeText(this, "Modifier la photo de profil", Toast.LENGTH_SHORT).show()
            // Implémentez ici l'ouverture de l'écran ou le dialogue pour changer la photo
        }
        /*-----------------------------------------------------------------------------------------------------------------*/

        /*----------------------------------- Paramètres ------------------------------------------------------------------*/

        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Notifications activées", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications désactivées", Toast.LENGTH_SHORT).show()
            }
        }

        //delete account
        parameter.findViewById<TextView>(R.id.deleteCompte).setOnClickListener {
            // logique pour supprimer le compte
            Toast.makeText(this, "implémenter cet partie", Toast.LENGTH_SHORT).show()
        }

        // logout
        parameter.findViewById<TextView>(R.id.logout).setOnClickListener {
            sessionManager.clearSession()
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }

        /*-----------------------------------------------------------------------------------------------------*/


        /* Header de la page profile*/
        // Retour à la page home
        backToHomePage.setOnClickListener {
            val intent = Intent(this, TimelineActivity::class.java)
            startActivity(intent)
        }
    }
}