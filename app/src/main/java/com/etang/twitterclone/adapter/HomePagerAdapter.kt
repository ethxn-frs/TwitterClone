package com.etang.twitterclone.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.etang.twitterclone.fragments.FollowingPostsFragment
import com.etang.twitterclone.fragments.GeneralPostsFragment

class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GeneralPostsFragment()
            1 -> FollowingPostsFragment()
            else -> throw IllegalStateException("Position inconnue : $position")
        }
    }
}