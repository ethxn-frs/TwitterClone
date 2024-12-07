package com.etang.twitterclone.ui.main

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.etang.twitterclone.R

class RegisterActivity: AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Liaison avec les éléments de l'interface
        nameInput = findViewById(R.id.nameInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        registerButton = findViewById(R.id.registerButton)

        // Bouton d'inscription
        registerButton.setOnClickListener {
            val name = nameInput.text.toString()
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (registerUser(name, email, password)) {
                Toast.makeText(this, "Inscription réussie", Toast.LENGTH_SHORT).show()
                finish() // Retourner à la page de connexion
            } else {
                Toast.makeText(this, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(name: String, email: String, password: String): Boolean {
        return true;
    }
}