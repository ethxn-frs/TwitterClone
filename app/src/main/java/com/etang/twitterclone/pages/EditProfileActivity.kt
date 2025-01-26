package com.etang.twitterclone.pages

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.etang.twitterclone.R
import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.network.dto.UpdateUserDto
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.LoginViewModel
import com.etang.twitterclone.viewmodel.UpdateUserViewModel
import com.google.android.material.textfield.TextInputEditText

class EditProfileActivity: AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var editTextFirstName: TextInputEditText
    private lateinit var editTextLastName: TextInputEditText
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextUsername: TextInputEditText
    private lateinit var editTextPhone: TextInputEditText
    private lateinit var updateUserViewModel: UpdateUserViewModel
    private lateinit var userdata: UpdateUserDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_profile)
        sessionManager = SessionManager(this)
        val user = sessionManager.getUser()
        updateUserViewModel = ViewModelProvider(this).get(UpdateUserViewModel::class.java)

        // Initialisation des champs
        editTextFirstName = findViewById(R.id.inputFirstName)
        editTextLastName = findViewById(R.id.editLastName)
        editTextEmail = findViewById(R.id.editEmail)
        editTextUsername = findViewById(R.id.editUsername)
        editTextPhone = findViewById(R.id.inputPhonenumber)



        intialyseUserData(user)

        // Observer les résultats de la mise à jour
        observeUpdateResult()

        // Bouton Enregistrer
        val btnSave = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSave)
        btnSave.setOnClickListener {
            updateUserData()
        }
    }

    fun intialyseUserData(user: User?){
        if (user != null) {
            // Définir les informations utilisateur par défaut dans les champs
            editTextFirstName.setText(user.firstName)
            editTextLastName.setText(user.lastName)
            editTextEmail.setText(user.email)
            editTextUsername.setText(user.username)
            editTextPhone.setText(user.phoneNumber)
        } else {
            Toast.makeText(this, "Impossible de récupérer les informations utilisateur", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUserData() {
        val firstName = editTextFirstName.text.toString().trim()
        val lastName = editTextLastName.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val username = editTextUsername.text.toString().trim()
        val phoneNumber = editTextPhone.text.toString().trim()

        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || username.isBlank() || phoneNumber.isBlank()) {
            Toast.makeText(this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show()
            return
        }

        val updateUserDto = UpdateUserDto(
            firstName = firstName,
            lastName = lastName,
            email = email,
            username = username,
            phoneNumber = phoneNumber
        )

        // Appel du ViewModel pour mettre à jour les données utilisateur
        updateUserViewModel.updateUserData(updateUserDto)
    }

    private fun observeUpdateResult() {
        // Observer les résultats de la mise à jour
        updateUserViewModel.updateResult.observe(this) { user ->
            if (user != null) {
                Toast.makeText(this, "Informations modifiées avec succès !", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // Observer les erreurs
        updateUserViewModel.errorMessage.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this, "Erreur : $errorMessage", Toast.LENGTH_SHORT).show()
            }
        }
    }
}