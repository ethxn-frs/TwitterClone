package com.etang.twitterclone.pages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.etang.twitterclone.R
import com.etang.twitterclone.databinding.ActivityMainBinding
import com.etang.twitterclone.fragments.HomeFragment
import com.etang.twitterclone.fragments.ProfileFragment
import com.etang.twitterclone.fragments.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Charger HomeFragment par défaut
        loadFragment(HomeFragment())

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> loadFragment(HomeFragment())
                R.id.navigation_search -> loadFragment(SearchFragment())
                R.id.navigation_profile -> loadFragment(ProfileFragment())
                //R.id.navigation_notifications -> loadFragment(NotificationsFragment())
                //R.id.navigation_messages -> loadFragment(MessagesFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
