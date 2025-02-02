package com.etang.twitterclone.pages.post

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.PostsAdapter
import com.etang.twitterclone.adapter.SideMenuAdapter
import com.etang.twitterclone.data.model.Post
import com.etang.twitterclone.data.model.SideMenuItem
import com.etang.twitterclone.network.dto.auth_dto.LoginResponseDto
import com.etang.twitterclone.pages.ConversationsActivity
import com.etang.twitterclone.pages.ProfileActivity
import com.etang.twitterclone.pages.settings.SettingsActivity
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.PostViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.Console

class TimelineActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostsAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var sessionManager: SessionManager
    private lateinit var sideMenu: View
    private lateinit var headerLayout : androidx.constraintlayout.widget.ConstraintLayout
    private lateinit var tvTitle : TextView
    private lateinit var ivSettings :ImageView
    private lateinit var ivProfile : ImageView
    private lateinit var recyclerViewMenu: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        sessionManager = SessionManager(this)

        initializeViews()
        setupHeader()
        setupRecyclerView()
        setupFab()
        setupBottomNavigation()
        setupSwipeRefresh()
        observeViewModel()
        viewModel.fetchPosts()
        handleUserWelcome()
        setupRecyclerViewMenu()
        // Clics sur les options du menu
        setupMenuActions(sideMenu)
    }

    private fun initializeViews(){
        sideMenu = findViewById(R.id.side_menu)
        drawerLayout = findViewById(R.id.drawerLayout)
        headerLayout = findViewById(R.id.headerLayout)
        tvTitle = headerLayout.findViewById(R.id.tvHeaderTitle)
        ivSettings = headerLayout.findViewById(R.id.ivSettings)
        ivProfile = headerLayout.findViewById(R.id.ivProfile)
        recyclerViewMenu = findViewById(R.id.recyclerViewMenu)
    }



    private fun setupHeader() {
        tvTitle.text = getString(R.string.header_title_posts)
        ivSettings.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        ivProfile.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupMenuActions(sideMenu: View) {
        //val ivProfile = headerLayout.findViewById<ImageView>(R.id.ivProfile)
        //val ivSettings = headerLayout.findViewById<ImageView>(R.id.ivSettings)

        ivProfile.setOnClickListener {
            if (drawerLayout.isDrawerOpen(androidx.core.view.GravityCompat.START)) {
                drawerLayout.closeDrawer(androidx.core.view.GravityCompat.START)
            } else {
                drawerLayout.openDrawer(androidx.core.view.GravityCompat.START)
            }
        }

        ivSettings.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        sideMenu.findViewById<TextView>(R.id.parameter_and_confidentiality).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewPosts)
        adapter = PostsAdapter(
            sessionManager = sessionManager,
            onLikeClicked = { postId ->
                val userId = sessionManager.getUserId()
                viewModel.likePost(postId, userId)
            },
            onShareClicked = { post ->
                sharePost(post)
            },
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupRecyclerViewMenu(){
        val menuItems = getMenuItems()
        recyclerViewMenu.layoutManager = LinearLayoutManager(this)
        recyclerViewMenu.adapter = SideMenuAdapter(menuItems)

    }

    private fun setupFab() {
        val fabCreatePost = findViewById<FloatingActionButton>(R.id.fabCreatePost)
        fabCreatePost.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> true
                R.id.navigation_messages -> {
                    val intent = Intent(this, ConversationsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun setupSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchPosts()
        }
    }

    private fun handleUserWelcome() {
        val userResponse: LoginResponseDto? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra("USER_DATA", LoginResponseDto::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getSerializableExtra("USER_DATA") as? LoginResponseDto
            }

        userResponse?.let {
            Toast.makeText(this, getString(R.string.welcome_message, it.user.username), Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        viewModel.posts.observe(this) { posts ->
            adapter.submitList(posts)
            swipeRefreshLayout.isRefreshing = false
        }

        viewModel.likeSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Post liked successfully!", Toast.LENGTH_SHORT).show()
                viewModel.fetchPosts() // Rafraîchir la liste après un like
            } else {
                Toast.makeText(this, "Failed to like the post.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun sharePost(post: Post) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Check out this tweet!")
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out this tweet by ${post.author.username}:\n\n${post.content}\n\nShared via TwitterClone"
            )
        }
        startActivity(Intent.createChooser(shareIntent, "Share post via"))
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


    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        // Appliquer les options d'animation pour la transition
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        )
        startActivity(intent, options.toBundle())
    }

}
