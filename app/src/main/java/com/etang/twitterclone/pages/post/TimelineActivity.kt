package com.etang.twitterclone.pages.post

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.PostsAdapter
import com.etang.twitterclone.viewmodel.PostViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TimelineActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostsAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        // Configurer RecyclerView
        recyclerView = findViewById(R.id.recyclerViewPosts)
        adapter = PostsAdapter()

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
    }
}
