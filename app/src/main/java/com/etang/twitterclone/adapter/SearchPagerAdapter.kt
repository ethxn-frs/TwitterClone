package com.etang.twitterclone.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.etang.twitterclone.fragments.SearchPostsFragment
import com.etang.twitterclone.fragments.SearchUsersFragment

class SearchPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SearchPostsFragment()
            1 -> SearchUsersFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
