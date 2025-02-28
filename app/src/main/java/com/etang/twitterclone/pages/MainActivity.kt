package com.etang.twitterclone.pages

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.etang.twitterclone.R
import com.etang.twitterclone.data.dto.UserProfileDto
import com.etang.twitterclone.databinding.ActivityMainBinding
import com.etang.twitterclone.fragments.HomeFragment
import com.etang.twitterclone.fragments.ProfileFragment
import com.etang.twitterclone.fragments.SearchFragment
import com.etang.twitterclone.fragments.SettingsFragment
import com.etang.twitterclone.session.SessionManager
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var ivProfileTopBar: ImageView
    private lateinit var tvTitleTopBar: TextView
    private lateinit var btnActionTopBar: ImageButton
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var btnBackTopBar: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Récupérer les vues de la Top Bar
        ivProfileTopBar = findViewById(R.id.ivProfileTopBar)
        tvTitleTopBar = findViewById(R.id.tvTitleTopBar)
        btnActionTopBar = findViewById(R.id.btnActionTopBar)
        btnBackTopBar = findViewById(R.id.btnBackTopBar)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        btnBackTopBar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        ivProfileTopBar.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Charger HomeFragment par défaut avec la bonne Top Bar
        updateTopBar(
            "Twitter Clone",
            "https://pbs.twimg.com/profile_images/438072985115959299/uWVK718p.jpeg",
            R.drawable.ic_edit_24px
        )
        loadFragment(HomeFragment())
        updateNavHeader()
        setupNavHeaderListeners()

        // Gérer les clics de la Side Bar (Navigation Drawer)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleMenuClick(menuItem)
            true
        }

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
        val topBar = findViewById<View>(R.id.topBar)

        if (fragment is ProfileFragment) {
            topBar.visibility = View.GONE
        } else {
            topBar.visibility = View.VISIBLE
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    public fun updateTopBar(
        title: String,
        profilePictureUrl: String,
        actionIcon: Int,
        isSettings: Boolean = false
    ) {
        tvTitleTopBar.text = title
        btnActionTopBar.setImageResource(actionIcon)

        if (isSettings) {
            ivProfileTopBar.visibility = View.GONE
            btnBackTopBar.visibility = View.VISIBLE
        } else {
            ivProfileTopBar.visibility = View.VISIBLE
            btnBackTopBar.visibility = View.GONE

            // Charger l'image de profil avec Glide
            Glide.with(this)
                .load(profilePictureUrl)
                .placeholder(R.drawable.ic_profile)
                .into(ivProfileTopBar)
        }
    }

    private fun updateNavHeader() {
        val headerView = navigationView.getHeaderView(0)

        val ivNavProfilePicture = headerView.findViewById<ImageView>(R.id.ivNavProfilePicture)
        val tvNavFullName = headerView.findViewById<TextView>(R.id.tvNavFullName)
        val tvNavUsername = headerView.findViewById<TextView>(R.id.tvNavUsername)
        val tvFollowingCount = headerView.findViewById<TextView>(R.id.tvFollowingCount)
        val tvFollowersCount = headerView.findViewById<TextView>(R.id.tvFollowersCount)

        val user = UserProfileDto(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            username = "johndoe",
            profilePictureUrl = "https://pbs.twimg.com/profile_images/438072985115959299/uWVK718p.jpeg",
            followersCount = 456,
            followingCount = 123,
            bio = "Développeur passionné 🚀",
            location = "Paris, France",
            website = "https://ethanfrancois.fr",
            birthDate = "2003-01-01",
            createdAt = "2020-01-01",
            coverPictureUrl = "https://pbs.twimg.com/profile_banners/1/1345634567/1500x500"
        )

        Glide.with(this).load(user.profilePictureUrl).into(ivNavProfilePicture)
        tvNavFullName.text = "${user.firstName} ${user.lastName}"
        tvNavUsername.text = "@${user.username}"
        tvFollowingCount.text = user.followingCount.toString()
        tvFollowersCount.text = user.followersCount.toString()
    }

    private fun setupNavHeaderListeners() {
        val headerView = navigationView.getHeaderView(0)

        val ivNavProfilePicture = headerView.findViewById<ImageView>(R.id.ivNavProfilePicture)
        val tvNavFullName = headerView.findViewById<TextView>(R.id.tvNavFullName)
        val tvNavUsername = headerView.findViewById<TextView>(R.id.tvNavUsername)

        val navigateToProfile = View.OnClickListener {
            loadFragment(ProfileFragment())
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        ivNavProfilePicture.setOnClickListener(navigateToProfile)
        tvNavFullName.setOnClickListener(navigateToProfile)
        tvNavUsername.setOnClickListener(navigateToProfile)
    }


    private fun handleMenuClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.menu_home -> loadFragment(HomeFragment())
            R.id.menu_search -> loadFragment(SearchFragment())
            R.id.menu_notifications -> {
                // À implémenter plus tard
            }

            R.id.menu_messages -> {
                // À implémenter plus tard
            }

            R.id.menu_bookmarks -> {
                // À implémenter plus tard
            }

            R.id.menu_profile -> loadFragment(ProfileFragment())
            R.id.menu_settings -> {
                loadFragment(SettingsFragment())
            }

            R.id.menu_logout -> {
                // Gérer la déconnexion (SessionManager)
                //sessionManager.logout()
                finish()
            }
        }
    }
}
