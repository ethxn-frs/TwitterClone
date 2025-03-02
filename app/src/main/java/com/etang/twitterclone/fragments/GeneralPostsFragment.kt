package com.etang.twitterclone.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.PostsAdapter
import com.etang.twitterclone.data.model.Post
import com.etang.twitterclone.databinding.FragmentPostsBinding
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.PostViewModel

class GeneralPostsFragment : Fragment() {

    private var _binding: FragmentPostsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PostViewModel by viewModels()
    private lateinit var adapter: PostsAdapter
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        adapter = PostsAdapter(
            sessionManager = sessionManager,
            onLikeClicked = { postId ->
                viewModel.likePost(postId, sessionManager.getUserId())
            },
            onShareClicked = { post ->
                sharePost(post)
            },
            onProfileClicked = { userId ->
                openUserProfile(userId)
            }
        )

        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPosts.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchPosts()
        }

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        viewModel.fetchPosts()
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
        startActivity(Intent.createChooser(shareIntent, "Partager via"))
    }

    private fun openUserProfile(userId: Int) {
        val fragment = ProfileFragment().apply {
            arguments = Bundle().apply {
                putInt("USER_ID", userId)
            }
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
