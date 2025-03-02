package com.etang.twitterclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.UsersAdapter
import com.etang.twitterclone.databinding.FragmentFollowersFollowingBinding
import com.etang.twitterclone.repositories.UserRepository
import kotlinx.coroutines.launch

class FollowersFollowingFragment : Fragment() {

    private var _binding: FragmentFollowersFollowingBinding? = null
    private val binding get() = _binding!!
    private val userRepository = UserRepository()
    private var userId: Int = 0
    private var type: String = "followers"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            userId = it.getInt("USER_ID")
            type = it.getString("TYPE", "followers")
        }

        setupTopBar()
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(requireContext())

        val adapter = UsersAdapter { selectedUserId ->
            openUserProfile(selectedUserId)
        }

        binding.recyclerViewUsers.adapter = adapter
        fetchUsers(adapter)
    }

    private fun setupTopBar() {
        val topBar = requireActivity().findViewById<View>(R.id.topBar)
        val tvTitleTopBar =
            requireActivity().findViewById<android.widget.TextView>(R.id.tvTitleTopBar)
        val btnBackTopBar =
            requireActivity().findViewById<android.widget.ImageButton>(R.id.btnBackTopBar)
        val ivProfileTopBar =
            requireActivity().findViewById<android.widget.ImageView>(R.id.ivProfileTopBar)
        val btnActionTopBar =
            requireActivity().findViewById<android.widget.ImageButton>(R.id.btnActionTopBar)

        topBar.visibility = View.VISIBLE
        tvTitleTopBar.text = if (type == "followers") "Abonnés" else "Abonnements"

        btnBackTopBar.visibility = View.VISIBLE
        ivProfileTopBar.visibility = View.GONE
        btnActionTopBar.visibility = View.GONE

        btnBackTopBar.setOnClickListener {
            topBar.visibility = View.GONE
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun fetchUsers(adapter: UsersAdapter) {
        lifecycleScope.launch {
            val users = if (type == "followers") {
                userRepository.getFollowers(userId)
            } else {
                userRepository.getFollowings(userId)
            }
            adapter.submitList(users)
        }
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

    companion object {
        fun newInstance(userId: Int, type: String): FollowersFollowingFragment {
            return FollowersFollowingFragment().apply {
                arguments = Bundle().apply {
                    putInt("USER_ID", userId)
                    putString("TYPE", type)
                }
            }
        }
    }
}
