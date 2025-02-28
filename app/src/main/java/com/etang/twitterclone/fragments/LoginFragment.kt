package com.etang.twitterclone.pages.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.etang.twitterclone.R
import com.etang.twitterclone.pages.AuthActivity
import com.etang.twitterclone.pages.MainActivity
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.LoginViewModel

class LoginFragment : Fragment() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        val emailInput = view.findViewById<EditText>(R.id.emailInput)
        val passwordInput = view.findViewById<EditText>(R.id.passwordInput)
        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val registerButton = view.findViewById<Button>(R.id.switchToRegisterButton)

        loginViewModel.loginResult.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                Toast.makeText(
                    requireContext(),
                    "Bienvenue ${user.user.username}",
                    Toast.LENGTH_SHORT
                ).show()
                sessionManager.saveUserSession(user.token, user.user)
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "Échec de la connexion", Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
        }

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Veuillez remplir tous les champs",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                loginViewModel.login(email, password)
            }
        }

        registerButton.setOnClickListener {
            (activity as? AuthActivity)?.switchToRegister()
        }
    }
}
