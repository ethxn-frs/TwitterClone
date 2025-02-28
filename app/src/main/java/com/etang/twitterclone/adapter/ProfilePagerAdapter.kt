package com.etang.twitterclone.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.etang.twitterclone.fragments.UserCommentsFragment
import com.etang.twitterclone.fragments.UserLikesFragment
import com.etang.twitterclone.fragments.UserPostsFragment

class ProfilePagerAdapter(fragment: Fragment, private val userId: Int) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UserPostsFragment.newInstance(userId)
            1 -> UserCommentsFragment.newInstance(userId)
            2 -> UserLikesFragment.newInstance(userId)
            else -> throw IllegalStateException("Position inconnue : $position")
        }
    }
}
