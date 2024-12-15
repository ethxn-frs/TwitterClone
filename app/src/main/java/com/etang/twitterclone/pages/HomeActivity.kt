package com.etang.twitterclone.pages

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.etang.twitterclone.R
import com.etang.twitterclone.network.dto.auth_dto.LoginDto
import com.etang.twitterclone.network.dto.auth_dto.LoginResponseDto
import java.io.Serializable

class HomeActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("HomeActivity","nous somme dans home Acticity")
        val user: Serializable? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            intent.getSerializableExtra("USER_DATA", LoginResponseDto::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("USER_DATA")
        }

        if (user != null) {
            Toast.makeText(this, "Bienvenue ${user} dans l'application", Toast.LENGTH_SHORT).show()
            // Utilisez les données utilisateur ici
        } else {
            Toast.makeText(this, "Erreur lors de la récupération des données utilisateur", Toast.LENGTH_SHORT).show()
        }
    }
}