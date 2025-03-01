package com.etang.twitterclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.PostsAdapter
import com.etang.twitterclone.databinding.FragmentUserCommentsBinding
import com.etang.twitterclone.repositories.UserRepository
import com.etang.twitterclone.session.SessionManager
import kotlinx.coroutines.launch

class UserCommentsFragment : Fragment() {

    private var _binding: FragmentUserCommentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PostsAdapter
    private lateinit var sessionManager: SessionManager
    private val userRepository = UserRepository()
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        userId = arguments?.getInt("USER_ID") ?: sessionManager.getUserId()

        adapter = PostsAdapter(
            sessionManager = sessionManager,
            onLikeClicked = {},
            onShareClicked = {},
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


        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPosts.adapter = adapter

        fetchUserComments()
    }

    private fun fetchUserComments() {
        lifecycleScope.launch {
            val posts = userRepository.getUserComments(userId)
            adapter.submitList(posts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(userId: Int): UserCommentsFragment {
            val fragment = UserCommentsFragment()
            val args = Bundle()
            args.putInt("USER_ID", userId)
            fragment.arguments = args
            return fragment
        }
    }
}

