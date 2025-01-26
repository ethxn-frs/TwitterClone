package com.etang.twitterclone.pages.post

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.PostsAdapter
import com.etang.twitterclone.adapter.SideMenuAdapter
import com.etang.twitterclone.data.model.SideMenuItem
import com.etang.twitterclone.network.dto.auth_dto.LoginResponseDto
import com.etang.twitterclone.pages.ProfileActivity
import com.etang.twitterclone.pages.menu.SideMenuActivity
import com.etang.twitterclone.pages.settings.SettingsActivity
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.PostViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TimelineActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostsAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var sessionManager: SessionManager
    private lateinit var drawerLayout: DrawerLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)

        setContentView(R.layout.activity_timeline)

        // Configurer Toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialisation des vues
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        recyclerView = findViewById(R.id.recyclerViewPosts)
        drawerLayout = findViewById(R.id.drawerLayout)
        //val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val headerLayout: View = findViewById(R.id.headerLayout)
        val sideMenu: View = findViewById(R.id.side_menu)
        val recyclerViewMenu: RecyclerView = findViewById(R.id.recyclerViewMenu)
        val fabCreatePost = findViewById<FloatingActionButton>(R.id.fabCreatePost)

        // Configuration de l'ActionBarDrawerToggle
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.drawer_open, R.string.drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Configuration RecyclerView principal
        adapter = PostsAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Configuration RecyclerView du menu latéral
        val menuItems = getMenuItems()
        recyclerViewMenu.layoutManager = LinearLayoutManager(this)
        recyclerViewMenu.adapter = SideMenuAdapter(menuItems)

        // Configuration du FloatingActionButton
        fabCreatePost.setOnClickListener {
            startActivity(Intent(this, CreatePostActivity::class.java))
        }

        // Configuration du SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchPosts()
        }

        // Mise à jour des informations utilisateur dans le menu latéral
        updateUserInfo(headerLayout, sideMenu)

        // Observateur des données
        observeViewModel()
        viewModel.fetchPosts()

        // Clics sur les options du menu
        setupMenuActions(sideMenu, headerLayout)
    }

    override fun startActivity(intent: Intent?) {
        val options = ActivityOptions.makeCustomAnimation(
            this,
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        )
        startActivity(intent, options.toBundle())
    }


    private fun observeViewModel() {
        viewModel.posts.observe(this) { posts ->
            adapter.submitList(posts)
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun updateUserInfo(headerLayout: View, sideMenu: View) {
        val user = sessionManager.getUser()
        // Vérifiez si le menu est bien trouvé
        if (user != null) {
            val userNameTextView = sideMenu.findViewById<TextView>(R.id.tvUserName)
            val userHandleTextView = sideMenu.findViewById<TextView>(R.id.tvUserHandle)

            if (userNameTextView != null && userHandleTextView != null) {
                userNameTextView.text = getString(R.string.user_name_format, user.firstName, user.lastName)
                userHandleTextView.text = getString(R.string.user_handle_format, user.username)
            } else {
                // Logs pour debug
                Log.e("TimelineActivity", "Erreur : TextViews du menu latéral introuvables")
            }
        } else {
            Log.e("TimelineActivity", "Erreur : Utilisateur introuvable dans SessionManager")
        }
        headerLayout.findViewById<TextView>(R.id.tvHeaderTitle).text = "Post"
    }

    private fun setupMenuActions(sideMenu: View, headerLayout: View) {
        val ivProfile = headerLayout.findViewById<ImageView>(R.id.ivProfile)
        val ivSettings = headerLayout.findViewById<ImageView>(R.id.ivSettings)

        ivProfile?.setOnClickListener {
            if (drawerLayout.isDrawerOpen(androidx.core.view.GravityCompat.START)) {
                drawerLayout.closeDrawer(androidx.core.view.GravityCompat.START)
            } else {
                drawerLayout.openDrawer(androidx.core.view.GravityCompat.START)
            }
        }?: run {
            Log.e("TimelineActivity", "ivProfile is null")
        }

        ivSettings?.setOnClickListener {
            Log.e("TimelineActivity", "le bouton paramètre est clicker ")
            startActivity(Intent(this, ProfileActivity::class.java))
        }?: run {
            Log.e("TimelineActivity", "ivSettings is null")
        }

        sideMenu.findViewById<TextView>(R.id.parameter_and_confidentiality).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun getMenuItems(): List<SideMenuItem> {
        return listOf(
            SideMenuItem(R.drawable.ic_profile, "Profil") {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            },
            SideMenuItem(R.drawable.ic_star, "Premium") {
                Toast.makeText(this, "Premium sélectionné", Toast.LENGTH_SHORT).show()
            },
            SideMenuItem(R.drawable.ic_save, "Signets") {
                Toast.makeText(this, "Signets sélectionné", Toast.LENGTH_SHORT).show()
            },
            SideMenuItem(R.drawable.ic_job, "Offres d'emploi") {
                Toast.makeText(this, "Offres d'emploi sélectionné", Toast.LENGTH_SHORT).show()
            },
            SideMenuItem(R.drawable.ic_money, "Monétisation") {
                Toast.makeText(this, "Monétisation sélectionnée", Toast.LENGTH_SHORT).show()
            },
            SideMenuItem(R.drawable.ic_list, "Liste"){
                Toast.makeText(this, "List sélectionnée", Toast.LENGTH_SHORT).show()
            },
            SideMenuItem(R.drawable.ic_space, "Spaces"){
                Toast.makeText(this, "space sélectionnée", Toast.LENGTH_SHORT).show()
            },
            SideMenuItem(R.drawable.ic_money, "Monétisation"){
                Toast.makeText(this, "Monétisation sélectionnée", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
