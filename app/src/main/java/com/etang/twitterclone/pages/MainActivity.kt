package com.etang.twitterclone.pages

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.etang.twitterclone.R
import com.etang.twitterclone.databinding.ActivityMainBinding
import com.etang.twitterclone.fragments.HomeFragment
import com.etang.twitterclone.fragments.ProfileFragment
import com.etang.twitterclone.fragments.SearchFragment
import com.etang.twitterclone.session.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var ivProfileTopBar: ImageView
    private lateinit var tvTitleTopBar: TextView
    private lateinit var btnActionTopBar: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Récupérer les vues de la Top Bar
        ivProfileTopBar = findViewById(R.id.ivProfileTopBar)
        tvTitleTopBar = findViewById(R.id.tvTitleTopBar)
        btnActionTopBar = findViewById(R.id.btnActionTopBar)

        // Charger HomeFragment par défaut avec la bonne Top Bar
        //updateTopBar("TwitterClone", sessionManager.getProfilePicture(), R.drawable.ic_star)
        updateTopBar(
            "Twitter Clone",
            "https://pbs.twimg.com/profile_images/438072985115959299/uWVK718p.jpeg",
            R.drawable.ic_edit_24px
        )
        loadFragment(HomeFragment())

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    updateTopBar(
                        "TwitterClone",
                        "https://pbs.twimg.com/profile_images/438072985115959299/uWVK718p.jpeg",
                        R.drawable.ic_edit_24px
                    )
                    loadFragment(HomeFragment())
                }

                R.id.navigation_search -> {
                    updateTopBar(
                        "Recherche",
                        "https://pbs.twimg.com/profile_images/438072985115959299/uWVK718p.jpeg",
                        R.drawable.ic_edit_24px
                    )
                    loadFragment(SearchFragment())
                }

                R.id.navigation_profile -> {
                    updateTopBar(
                        "Profil",
                        "https://pbs.twimg.com/profile_images/438072985115959299/uWVK718p.jpeg",
                        R.drawable.baseline_heart_broken_24
                    )
                    loadFragment(ProfileFragment())
                }
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun updateTopBar(title: String, profilePictureUrl: String, actionIcon: Int) {
        tvTitleTopBar.text = title
        btnActionTopBar.setImageResource(actionIcon)

        // Charger l'image de profil avec Glide
        Glide.with(this)
            .load(profilePictureUrl)
            .placeholder(R.drawable.ic_profile)
            .into(ivProfileTopBar)
    }
}
