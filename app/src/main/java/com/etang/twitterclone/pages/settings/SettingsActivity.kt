package com.etang.twitterclone.pages.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.SettingsAdapter
import com.etang.twitterclone.data.model.SettingItem
import com.etang.twitterclone.pages.post.TimelineActivity

class SettingsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        // Configuration de la toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Liste des paramètres
        val settings = listOf(
            SettingItem(
                icon = R.drawable.ic_account,
                title = "Votre compte",
                description = "Affichez les informations de votre compte",
                action = { Toast.makeText(this, "Votre compte", Toast.LENGTH_SHORT).show() }
            ),
            SettingItem(
                icon = R.drawable.ic_security,
                title = "Sécurité et accès au compte",
                description = "Gérez la sécurité de votre compte",
                action = { Toast.makeText(this, "Sécurité et accès", Toast.LENGTH_SHORT).show() }
            ),
            SettingItem(
                icon = R.drawable.ic_monetization,
                title = "Monétisation",
                description = "Gérez vos options de monétisation",
                action = { Toast.makeText(this, "Monétisation", Toast.LENGTH_SHORT).show() }
            ),
            SettingItem(
                icon = R.drawable.ic_star,
                title = "Premuim",
                description = "Gérez les fonctionnalités de votre abonnement",
                action = { Toast.makeText(this, "Monétisation", Toast.LENGTH_SHORT).show() }
            ),
            SettingItem(
                icon = R.drawable.ic_fil,
                title = "Fil",
                description = "Configurez l'apparence de votre fil et des intéractions",
                action = { Toast.makeText(this, "Monétisation", Toast.LENGTH_SHORT).show() }
            ),
            SettingItem(
                icon = R.drawable.ic_confidentiality,
                title = "confidentialité et sécurité",
                description = "Gérez les informations que vous voyez et partagez sur twitter-clone",
                action = { Toast.makeText(this, "Monétisation", Toast.LENGTH_SHORT).show() }
            ),
            SettingItem(
                icon = R.drawable.ic_notifications_24px,
                title = "Notifiactions",
                description = "Gérez les informations que vous voyez et partagez sur twitter-clone",
                action = { Toast.makeText(this, "Monétisation", Toast.LENGTH_SHORT).show() }
            ),
            SettingItem(
                icon = R.drawable.ic_accesibility,
                title = "Accesibilité, affichage et Langues",
                description = "Gérez les informations que vous voyez et partagez sur twitter-clone",
                action = { Toast.makeText(this, "Monétisation", Toast.LENGTH_SHORT).show() }
            ),
            SettingItem(
                icon = R.drawable.ic_resource,
                title = "Resources supplémentaires",
                description = "Gérez les informations que vous voyez et partagez sur twitter-clone",
                action = { Toast.makeText(this, "Monétisation", Toast.LENGTH_SHORT).show() }
            )
        )

        findViewById<Toolbar>(R.id.toolbar).setOnClickListener{
            val intent = Intent(this, TimelineActivity::class.java)
            startActivity(intent)
        }

        // Configuration du RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewSettings)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SettingsAdapter(settings)
    }
}