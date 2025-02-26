package com.etang.twitterclone.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.etang.twitterclone.fragments.ProfileFragment
import com.etang.twitterclone.fragments.UserCommentsFragment
import com.etang.twitterclone.fragments.UserLikesFragment
import com.etang.twitterclone.fragments.UserPostsFragment

class ProfilePagerAdapter(fragment: ProfileFragment) : FragmentStateAdapter(fragment) {

    private val tabs =
        listOf(UserPostsFragment(), UserCommentsFragment(), UserLikesFragment())

    override fun getItemCount() = tabs.size

    override fun createFragment(position: Int): Fragment {
        return tabs[position]
    }
}