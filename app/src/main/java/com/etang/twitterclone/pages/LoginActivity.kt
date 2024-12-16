package com.etang.twitterclone.pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.etang.twitterclone.R
import com.etang.twitterclone.pages.main.MainActivity
import com.etang.twitterclone.pages.post.TimelineActivity
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.LoginViewModel

class LoginActivity:AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(this)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        val RegisterButton = findViewById<Button>(R.id.ButtonRegister)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)

        // Observer les données utilisateur
        loginViewModel.loginResult.observe(this) { user ->
            if (user != null) {
                Toast.makeText(this, "Bienvenue ${user.user.username}", Toast.LENGTH_SHORT).show()

                sessionManager.saveUserSession(user.token, user.user)

                val intent = Intent(this, TimelineActivity::class.java)
                intent.putExtra("USER_DATA", user)
                startActivity(intent)
                finish()
            }else {
                Toast.makeText(this, "Échec de la connexion. Veuillez réessayer.", Toast.LENGTH_SHORT).show()
            }
        }

        // Observer les erreurs
        loginViewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            } else {
                loginViewModel.login(email, password)
            }
        }

        // rediriger vers la page de création de compte
        RegisterButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}