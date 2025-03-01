package com.etang.twitterclone.pages

import android.content.Intent
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
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.etang.twitterclone.R
import com.etang.twitterclone.databinding.ActivityMainBinding
import com.etang.twitterclone.fragments.HomeFragment
import com.etang.twitterclone.fragments.ProfileFragment
import com.etang.twitterclone.fragments.SearchFragment
import com.etang.twitterclone.fragments.SettingsFragment
import com.etang.twitterclone.repositories.UserRepository
import com.etang.twitterclone.session.SessionManager
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var ivProfileTopBar: ImageView
    private lateinit var tvTitleTopBar: TextView
    private lateinit var btnActionTopBar: ImageButton
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var btnBackTopBar: ImageButton
    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
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

        updateTopBar(
            "Twitter Clone",
            R.drawable.ic_edit_24px
        )

        loadFragment(HomeFragment())
        updateNavHeader()
        setupNavHeaderListeners()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleMenuClick(menuItem)
            true
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    updateTopBar(
                        "TwitterClone", R.drawable.ic_edit_24px
                    )
                    loadFragment(HomeFragment())
                }

                R.id.navigation_search -> {
                    updateTopBar(
                        "Recherche", R.drawable.ic_edit_24px
                    )
                    loadFragment(SearchFragment())
                }

                R.id.navigation_profile -> {
                    updateTopBar(
                        "Profil", R.drawable.baseline_heart_broken_24
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

    fun updateTopBar(title: String, actionIcon: Int, isSettings: Boolean = false) {
        tvTitleTopBar.text = title
        btnActionTopBar.setImageResource(actionIcon)

        if (isSettings) {
            ivProfileTopBar.visibility = View.GONE
            btnBackTopBar.visibility = View.VISIBLE
        } else {
            ivProfileTopBar.visibility = View.VISIBLE
            btnBackTopBar.visibility = View.GONE

            lifecycleScope.launch {
                val user = userRepository.getUserById(sessionManager.getUserId())
                val profileImageUrl = user?.profilePictureUrl
                    ?: "https://img.lapresse.ca/435x290/201704/03/1378798-nouvelle-image-defaut-ressemble-davantage.jpg"

                Glide.with(this@MainActivity)
                    .load(profileImageUrl)
                    .placeholder(R.drawable.ic_profile)
                    .circleCrop()
                    .into(ivProfileTopBar)
            }
        }
    }


    private fun updateNavHeader() {
        lifecycleScope.launch {
            val headerView = navigationView.getHeaderView(0)

            val ivNavProfilePicture = headerView.findViewById<ImageView>(R.id.ivNavProfilePicture)
            val tvNavFullName = headerView.findViewById<TextView>(R.id.tvNavFullName)
            val tvNavUsername = headerView.findViewById<TextView>(R.id.tvNavUsername)
            val tvFollowingCount = headerView.findViewById<TextView>(R.id.tvFollowingCount)
            val tvFollowersCount = headerView.findViewById<TextView>(R.id.tvFollowersCount)

            val user = userRepository.getUserById(sessionManager.getUserId())

            if (user != null) {
                Glide.with(this@MainActivity)
                    .load(
                        user.profilePictureUrl
                            ?: "https://img.lapresse.ca/435x290/201704/03/1378798-nouvelle-image-defaut-ressemble-davantage.jpg"
                    )
                    .into(ivNavProfilePicture)

                tvNavFullName.text = "${user.firstName} ${user.lastName}"
                tvNavUsername.text = "@${user.username}"
                tvFollowingCount.text = user.following.size.toString()
                tvFollowersCount.text = user.followers.size.toString()
            }
        }
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
            R.id.menu_notifications -> { /* À implémenter plus tard */
            }

            R.id.menu_messages -> { /* À implémenter plus tard */
            }

            R.id.menu_bookmarks -> { /* À implémenter plus tard */
            }

            R.id.menu_profile -> loadFragment(ProfileFragment())
            R.id.menu_settings -> loadFragment(SettingsFragment())

            R.id.menu_logout -> {
                sessionManager.logout()
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }

}
