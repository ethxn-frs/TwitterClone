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