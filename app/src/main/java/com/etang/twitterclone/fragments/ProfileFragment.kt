package com.etang.twitterclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.etang.twitterclone.adapter.ProfilePagerAdapter
import com.etang.twitterclone.data.dto.UserProfileDto
import com.etang.twitterclone.databinding.FragmentProfileBinding
import com.google.android.material.tabs.TabLayoutMediator

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = UserProfileDto(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            username = "johndoe",
            bio = "Passionné par la tech 🚀",
            location = "Paris, France",
            website = "https://ethanfrancois.fr",
            birthDate = "2003-01-01",
            createdAt = "2020-01-01",
            coverPictureUrl = "https://example.com/cover.jpg",
            profilePictureUrl = "https://example.com/profile.jpg",
            followersCount = 123,
            followingCount = 456
        )

        bindUserProfile(user)
        val tabTitles = arrayOf("Posts", "Réponses", "Likes")
        val adapter = ProfilePagerAdapter(this)
        binding.viewPagerProfile.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPagerProfile) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun bindUserProfile(user: UserProfileDto) {
        binding.tvFullName.text = "${user.firstName} ${user.lastName}"
        binding.tvUsername.text = "@${user.username}"
        binding.tvBio.text = user.bio
        binding.tvLocation.text = user.location
        binding.tvWebsite.text = user.website
        binding.tvBirthdate.text = user.birthDate
        binding.tvJoinedDate.text = "A rejoint TwitterClone le ${user.createdAt}"
        binding.tvFollowersCount.text = user.followersCount.toString()
        binding.tvFollowingCount.text = user.followingCount.toString()

        Glide.with(this).load(user.coverPictureUrl).into(binding.ivCover)
        Glide.with(this).load(user.profilePictureUrl).into(binding.ivProfilePicture)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
