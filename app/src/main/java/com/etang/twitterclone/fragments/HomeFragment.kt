package com.etang.twitterclone.fragments

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.PostsAdapter
import com.etang.twitterclone.data.model.Post
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
            onProfileClicked = { userId ->
                val fragment = ProfileFragment().apply {
                    arguments = Bundle().apply {
                        putInt("USER_ID", userId)
                    }
                }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchPosts()
        }

        observeViewModel()
        viewModel.fetchPosts()

        val fabCreatePost = view.findViewById<FloatingActionButton>(R.id.fabCreatePost)
        fabCreatePost.setOnClickListener {
            val intent = Intent(requireContext(), CreatePostActivity::class.java)
            startActivity(intent)
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val topBar = requireActivity().findViewById<View>(R.id.topBar)

                if (dy > 0) {
                    // Scroll vers le bas → on cache la Top Bar et enlève la marge
                    topBar.animate().translationY(-topBar.height.toFloat()).setDuration(200).start()
                    updateSwipeRefreshMargin(false) // Enlève la marge
                } else if (dy < 0) {
                    // Scroll vers le haut → on affiche la Top Bar et remet la marge
                    topBar.animate().translationY(0f).setDuration(200).start()
                    updateSwipeRefreshMargin(true) // Remet la marge
                }
            }
        })
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

    private fun updateSwipeRefreshMargin(showTopBar: Boolean) {
        val swipeRefreshLayout =
            view?.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout) ?: return
        val layoutParams = swipeRefreshLayout.layoutParams as ViewGroup.MarginLayoutParams

        val newMarginTop =
            if (showTopBar) requireActivity().findViewById<View>(R.id.topBar).height else 0

        if (layoutParams.topMargin != newMarginTop) {
            ValueAnimator.ofInt(layoutParams.topMargin, newMarginTop).apply {
                duration = 200
                addUpdateListener { animation ->
                    layoutParams.topMargin = animation.animatedValue as Int
                    swipeRefreshLayout.layoutParams = layoutParams
                }
                start()
            }
        }
    }
}
