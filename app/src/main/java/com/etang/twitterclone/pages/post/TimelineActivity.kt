package com.etang.twitterclone.pages.post

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.PostsAdapter
import com.etang.twitterclone.network.dto.auth_dto.LoginResponseDto
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.PostViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TimelineActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostsAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        sessionManager = SessionManager(this)

        // Accéder à l'inclusion du header
        val headerLayout =
            findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.headerLayout)

        // Récupérer le TextView du Header
        // Récupérer le TextView à l'intérieur du header
        val tvTitle = headerLayout.findViewById<TextView>(R.id.tvHeaderTitle)
        tvTitle.text = "Post"

        val userResponse: LoginResponseDto? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra("USER_DATA", LoginResponseDto::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getSerializableExtra("USER_DATA") as? LoginResponseDto
            }

        userResponse?.let {
            Toast.makeText(this, "Bienvenue ${it.user.username} !", Toast.LENGTH_SHORT).show()
        }

        // Configurer RecyclerView
        recyclerView = findViewById(R.id.recyclerViewPosts)
        adapter = PostsAdapter(sessionManager) { postId ->
            val userId = sessionManager.getUserId();
            viewModel.likePost(postId, userId)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Configurer Toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Configurer FloatingActionButton
        val fabCreatePost = findViewById<FloatingActionButton>(R.id.fabCreatePost)
        fabCreatePost.setOnClickListener {

            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }

        // Configurer SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchPosts()
        }

        observeViewModel()
        viewModel.fetchPosts()
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
}
