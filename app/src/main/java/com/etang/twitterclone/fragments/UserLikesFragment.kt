package com.etang.twitterclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.etang.twitterclone.adapter.PostsAdapter
import com.etang.twitterclone.databinding.FragmentUserLikesBinding
import com.etang.twitterclone.databinding.FragmentUserPostsBinding
import com.etang.twitterclone.repositories.UserRepository
import com.etang.twitterclone.session.SessionManager
import kotlinx.coroutines.launch

class UserLikesFragment : Fragment() {

    private var _binding: FragmentUserLikesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PostsAdapter
    private lateinit var sessionManager: SessionManager
    private val userRepository = UserRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserLikesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        adapter = PostsAdapter(sessionManager, {}, {})

        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPosts.adapter = adapter

        fetchUserLikes()
    }

    private fun fetchUserLikes() {
        val userId = sessionManager.getUserId()

        lifecycleScope.launch {
            val posts = userRepository.getUserLikedPosts(userId)
            adapter.submitList(posts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
