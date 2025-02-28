package com.etang.twitterclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.etang.twitterclone.R
import com.etang.twitterclone.network.dto.auth_dto.RegisterDto
import com.etang.twitterclone.pages.AuthActivity
import com.etang.twitterclone.viewmodel.RegisterViewModel
import java.text.SimpleDateFormat

class RegisterFragment : Fragment() {
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        val firstNameInput = view.findViewById<EditText>(R.id.firstNameInput)
        val lastNameInput = view.findViewById<EditText>(R.id.lastNameInput)
        val usernameInput = view.findViewById<EditText>(R.id.usernameInput)
        val emailInput = view.findViewById<EditText>(R.id.emailInput)
        val passwordInput = view.findViewById<EditText>(R.id.passwordInput)
        val registerButton = view.findViewById<Button>(R.id.registerButton)
        val loginRedirectButton = view.findViewById<Button>(R.id.switchToLoginButton)
        val phoneInput = view.findViewById<EditText>(R.id.phoneInput)

        registerButton.setOnClickListener {
            val registerDto = RegisterDto(
                firstname = firstNameInput.text.toString(),
                lastname = lastNameInput.text.toString(),
                username = usernameInput.text.toString(),
                email = emailInput.text.toString(),
                password = passwordInput.text.toString(),
                birthDate = SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000"),
                phoneNumber = phoneInput.text.toString()
            )

            registerViewModel.registerUser(registerDto)
        }

        loginRedirectButton.setOnClickListener {
            (activity as? AuthActivity)?.switchToLogin()
        }
    }
}
