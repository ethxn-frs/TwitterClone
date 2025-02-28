package com.etang.twitterclone.pages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.etang.twitterclone.R
import com.etang.twitterclone.pages.auth.LoginFragment

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val showRegister = intent.getBooleanExtra("SHOW_REGISTER", false)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(
                    R.id.authFragmentContainer,
                    if (showRegister) RegisterFragment() else LoginFragment()
                )
            }
        }
    }

    fun switchToRegister() {
        supportFragmentManager.commit {
            replace(R.id.authFragmentContainer, RegisterFragment())
            addToBackStack(null)
        }
    }

    fun switchToLogin() {
        supportFragmentManager.popBackStack()
    }
}
