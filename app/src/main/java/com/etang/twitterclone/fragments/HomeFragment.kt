package com.etang.twitterclone.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.PostsAdapter
import com.etang.twitterclone.data.model.Post
import com.etang.twitterclone.pages.ProfileActivity
import com.etang.twitterclone.pages.post.CreatePostActivity
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.PostViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostsAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        val headerLayout =
            view.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.headerLayout)
        val tvTitle = headerLayout.findViewById<TextView>(R.id.tvHeaderTitle)
        tvTitle.text = "Post"

        val ivSettings = headerLayout.findViewById<ImageView>(R.id.ivSettings)

        recyclerView = view.findViewById(R.id.recyclerViewPosts)
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

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchPosts()
        }

        observeViewModel()
        viewModel.fetchPosts()

        ivSettings.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }

        val fabCreatePost = view.findViewById<FloatingActionButton>(R.id.fabCreatePost)
        fabCreatePost.setOnClickListener {
            val intent = Intent(requireContext(), CreatePostActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
            swipeRefreshLayout.isRefreshing = false
        }

        viewModel.likeSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Post liked successfully!", Toast.LENGTH_SHORT)
                    .show()
                viewModel.fetchPosts()
            } else {
                Toast.makeText(requireContext(), "Failed to like the post.", Toast.LENGTH_SHORT)
                    .show()
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
}
