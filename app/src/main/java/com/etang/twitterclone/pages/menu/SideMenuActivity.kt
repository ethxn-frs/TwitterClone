package com.etang.twitterclone.pages.menu

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.SideMenuAdapter
import com.etang.twitterclone.data.model.SideMenuItem

class SideMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_side_menu)

        // Exemple d'éléments du menu
       /* val menuItems = listOf(
            SideMenuItem(
                icon = R.drawable.ic_profile,
                title = "Profil",
                action = { Toast.makeText(this, "Profil sélectionné", Toast.LENGTH_SHORT).show() }
            ),
            SideMenuItem(
                icon = R.drawable.ic_star,
                title = "Premium",
                action = { Toast.makeText(this, "Premium sélectionné", Toast.LENGTH_SHORT).show() }
            ),
            SideMenuItem(
                icon = R.drawable.ic_save,
                title = "Signets",
                action = { Toast.makeText(this, "Signets sélectionné", Toast.LENGTH_SHORT).show() }
            ),
            SideMenuItem(
                icon = R.drawable.ic_job,
                title = "Offres d'emploi",
                action = { Toast.makeText(this, "Profil sélectionné", Toast.LENGTH_SHORT).show() }
            ),
            SideMenuItem(
                icon = R.drawable.ic_list,
                title = "Listes",
                action = { Toast.makeText(this, "Premium sélectionné", Toast.LENGTH_SHORT).show() }
            ),
            SideMenuItem(
                icon = R.drawable.ic_space,
                title = "Spaces",
                action = { Toast.makeText(this, "Signets sélectionné", Toast.LENGTH_SHORT).show() }
            ),
            SideMenuItem(
                icon = R.drawable.ic_money,
                title = "Monétisation",
                action = { Toast.makeText(this, "Signets sélectionné", Toast.LENGTH_SHORT).show() }
            )
        )

        // Configuration du RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewMenu)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SideMenuAdapter(menuItems)*/

        // Gérer les clics sur les éléments du footer
        findViewById<View>(R.id.parameter_and_confidentiality).setOnClickListener {
            Toast.makeText(this, "Réglages sélectionné", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.parameter_help).setOnClickListener {
            Toast.makeText(this, "Centre d'assistance sélectionné", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.parameter_achat).setOnClickListener {
            Toast.makeText(this, "Achats sélectionné", Toast.LENGTH_SHORT).show()
        }
    }
}