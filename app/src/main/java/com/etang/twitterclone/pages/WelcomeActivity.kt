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

        sessionManager = SessionManager(this)

        // Si l'utilisateur est déjà connecté, on le redirige vers MainActivity
        if (sessionManager.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

        loginButton.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java).apply {
                putExtra("SHOW_REGISTER", true)
            })
        }
    }
}