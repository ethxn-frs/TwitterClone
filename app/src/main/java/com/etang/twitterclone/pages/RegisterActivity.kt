package com.etang.twitterclone.pages

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.etang.twitterclone.R
import com.etang.twitterclone.network.dto.auth_dto.RegisterDto
import com.etang.twitterclone.pages.interfaces.RegisterHandler
import com.etang.twitterclone.viewmodel.LoginViewModel
import com.etang.twitterclone.viewmodel.RegisterViewModel
import java.text.SimpleDateFormat

class RegisterActivity: AppCompatActivity(), RegisterHandler{

    private lateinit var  registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()

        setContentView(R.layout.activity_register)

        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        val firstNameInput = findViewById<EditText>(R.id.firstNameInput)
        val lastNameInput = findViewById<EditText>(R.id.lastNameInput)
        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val phoneNumberInput = findViewById<EditText>(R.id.phoneNumberInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val birthDateInput = findViewById<EditText>(R.id.birthDateInput)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerViewModel.registrationSuccess.observe(this){ user ->
            if (user != null) {
                Toast.makeText(this, "Bienvenue ${user}", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("USER_DATA", user)
                startActivity(intent)
                finish()
            }else {
                Toast.makeText(this, "Échec de la connexion. Veuillez réessayer.", Toast.LENGTH_SHORT).show()
            }
        }

        registerViewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            try {
                val firstName = firstNameInput.text.toString()
                val lastName = lastNameInput.text.toString()
                val username = usernameInput.text.toString()
                val email = emailInput.text.toString()
                val phoneNumber = phoneNumberInput.text.toString()
                val password = passwordInput.text.toString()
                val birthDate = SimpleDateFormat("dd/MM/yyyy").parse(birthDateInput.text.toString())

                val registerDto = RegisterDto(
                    firstname = firstName,
                    lastname = lastName,
                    username = username,
                    email = email,
                    phoneNumber = phoneNumber,
                    password = password,
                    birthDate = birthDate
                )

                if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || birthDate.toString().isEmpty()){
                    Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()

                }else{
                    registerViewModel.registerUser(registerDto)
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Erreur : ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun displayRegisterPage() {
        TODO("Not yet implemented")
    }
}