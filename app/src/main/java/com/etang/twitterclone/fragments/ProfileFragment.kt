package com.etang.twitterclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.etang.twitterclone.adapter.ProfilePagerAdapter
import com.etang.twitterclone.data.dto.UserProfileDto
import com.etang.twitterclone.databinding.FragmentProfileBinding
import com.etang.twitterclone.repositories.UserRepository
import com.etang.twitterclone.session.SessionManager
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val userRepository = UserRepository()
    private lateinit var sessionManager: SessionManager
    private var userId: Int = 0  // ID de l'utilisateur du profil affiché
    private var currentUserId: Int = 0  // ID de l'utilisateur connecté

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        currentUserId = sessionManager.getUserId()

        // Récupérer l'ID du profil à afficher depuis les arguments
        userId = arguments?.getInt("USER_ID") ?: currentUserId

        fetchUserProfile(userId)

        val adapter = ProfilePagerAdapter(this, userId)
        binding.viewPagerProfile.adapter = adapter

        val tabTitles = arrayOf("Posts", "Réponses", "Likes")
        TabLayoutMediator(binding.tabLayout, binding.viewPagerProfile) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun fetchUserProfile(id: Int) {
        lifecycleScope.launch {
            val user = userRepository.getUserById(id)
            if (user != null) {
                bindUserProfile(user)
            }
        }
    }

    private fun bindUserProfile(user: UserProfileDto) {
        binding.tvFullName.text = "${user.firstName} ${user.lastName}"
        binding.tvUsername.text = "@${user.username}"
        binding.tvBio.text = user.bio ?: ""
        binding.tvLocation.text = user.location ?: ""
        binding.tvWebsite.text = user.website ?: ""
        binding.tvBirthdate.text = "🎂 " + user.birthDate
        binding.tvJoinedDate.text = "📅 Inscrit le ${user.createdAt}"
        binding.tvFollowersCount.text = user.followersCount.toString()
        binding.tvFollowingCount.text = user.followingCount.toString()

        Glide.with(this).load(user.coverPictureUrl).into(binding.ivCover)
        Glide.with(this).load(user.profilePictureUrl).into(binding.ivProfilePicture)

        if (user.id == currentUserId) {
            binding.btnFollow.text = "Modifier Profil"
        } else {
            binding.btnFollow.text = "Suivre"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
