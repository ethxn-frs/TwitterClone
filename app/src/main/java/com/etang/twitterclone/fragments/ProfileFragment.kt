package com.etang.twitterclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.ProfilePagerAdapter
import com.etang.twitterclone.data.dto.UserProfileDto
import com.etang.twitterclone.databinding.FragmentProfileBinding
import com.etang.twitterclone.repositories.UserRepository
import com.etang.twitterclone.session.SessionManager
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val userRepository = UserRepository()
    private lateinit var sessionManager: SessionManager
    private var userId: Int = 0
    private var currentUserId: Int = 0
    private var isFollowing = false

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
        userId = arguments?.getInt("USER_ID") ?: currentUserId

        fetchUserProfile(userId)

        val adapter = ProfilePagerAdapter(this, userId)
        binding.viewPagerProfile.adapter = adapter

        val tabTitles = arrayOf("Posts", "Réponses", "Likes")
        TabLayoutMediator(binding.tabLayout, binding.viewPagerProfile) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        binding.btnFollow.setOnClickListener { handleFollowUnfollow() }
    }

    private fun fetchUserProfile(id: Int) {
        lifecycleScope.launch {
            val user = userRepository.getUserById(id)
            if (user != null) {
                bindUserProfile(UserProfileDto(user))

                // Si on consulte notre propre profil, pas besoin de vérifier si on suit quelqu'un
                if (user.id != currentUserId) {
                    isFollowing = userRepository.isFollowing(currentUserId, userId)
                    updateFollowButton()
                } else {
                    setupEditProfileButton()
                }
            }
        }
    }

    private fun bindUserProfile(user: UserProfileDto) {
        binding.tvFullName.text = "${user.firstName} ${user.lastName}"
        binding.tvUsername.text = "@${user.username}"
        binding.tvBio.text = user.bio ?: ""
        binding.tvLocation.text = user.location ?: ""
        binding.tvWebsite.text = user.website ?: ""
        binding.tvBirthdate.text = "🎂 " + formatDate(user.birthDate)
        binding.tvJoinedDate.text = "📅 Inscrit le ${formatDate(user.createdAt)}"
        binding.tvFollowersCount.text = user.followersCount.toString()
        binding.tvFollowingCount.text = user.followingCount.toString()

        Glide.with(this).load(
            user.coverPictureUrl
                ?: "https://i.pinimg.com/736x/5a/a9/24/5aa92474e3a20a977dbdcf301d93b57d.jpg"
        ).into(binding.ivCover)
        Glide.with(this).load(
            user.profilePictureUrl
                ?: "https://img.lapresse.ca/435x290/201704/03/1378798-nouvelle-image-defaut-ressemble-davantage.jpg"
        ).into(binding.ivProfilePicture)

        binding.tvFollowersCount.setOnClickListener {
            openFollowersFollowingFragment(user.id, "followers")
        }
        binding.tvFollowersCount.setOnClickListener {
            openFollowersFollowingFragment(user.id, "followers")
        }

        binding.tvFollowingCount.setOnClickListener {
            openFollowersFollowingFragment(user.id, "following")
        }
    }

    private fun formatDate(isoDate: String?): String {
        return try {
            if (isoDate.isNullOrEmpty()) return ""

            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            isoFormat.timeZone = TimeZone.getTimeZone("UTC")

            val date = isoFormat.parse(isoDate)
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            date?.let { outputFormat.format(it) } ?: isoDate
        } catch (e: Exception) {
            isoDate ?: ""
        }
    }

    private fun setupEditProfileButton() {
        binding.btnFollow.text = "Modifier Profil"
        binding.btnFollow.setBackgroundColor(resources.getColor(android.R.color.holo_blue_light))
        binding.btnFollow.setOnClickListener {
            val fragment = EditProfileFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }


    private fun updateFollowButton() {
        if (isFollowing) {
            binding.btnFollow.text = "Désabonner"
            binding.btnFollow.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
        } else {
            binding.btnFollow.text = "Suivre"
            binding.btnFollow.setBackgroundColor(resources.getColor(android.R.color.holo_blue_light))
        }
    }

    private fun openFollowersFollowingFragment(userId: Int, type: String) {

        val fragment = FollowersFollowingFragment.newInstance(userId, type)

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun handleFollowUnfollow() {
        lifecycleScope.launch {
            val success = if (isFollowing) {
                userRepository.unfollowUser(currentUserId, userId)
            } else {
                userRepository.followUser(currentUserId, userId)
            }

            if (success) {
                isFollowing = !isFollowing
                updateFollowButton()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}