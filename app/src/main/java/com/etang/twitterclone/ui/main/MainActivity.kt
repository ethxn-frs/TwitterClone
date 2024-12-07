package com.etang.twitterclone.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.etang.twitterclone.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isUserLoggedIn()) {
            // Si l'utilisateur n'est pas connecté, redirige vers LoginActivity
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish() // Empêche de revenir à MainActivity sans se connecter
        } else {
            setContentView(R.layout.activity_main)
            // Logique pour l'utilisateur connecté (tableau de bord ou accueil)
        }
    }

    private fun isUserLoggedIn(): Boolean {

        return false // Remplacez par la logique réelle
    }
}