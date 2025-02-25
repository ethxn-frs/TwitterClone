package com.etang.twitterclone.pages

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.etang.twitterclone.R
import com.etang.twitterclone.session.SessionManager

class WelcomeActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

        sessionManager = SessionManager(this)

        if (sessionManager.isLoggedIn()) {
            // L'utilisateur est déjà connecté, redirection vers HomeActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        /*else {
            // L'utilisateur doit se connecter
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }*/

        // Redirige vers la page de connexion
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Redirige vers la page d'inscription
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}