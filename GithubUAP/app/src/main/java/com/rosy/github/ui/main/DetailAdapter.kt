package com.rosy.github.ui.main

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rosy.github.api.Const.TAB_TITLES
import com.rosy.github.ui.detail.FragmentFollowers
import com.rosy.github.ui.detail.FragmentFollowing

class DetailAdapter(activity: AppCompatActivity, private val username: String) :
    FragmentStateAdapter(activity){
    override fun getItemCount(): Int = TAB_TITLES.size
    override fun createFragment(position: Int): Fragment {
       var fragment: Fragment? = null
        when(position){
            0 -> fragment = FragmentFollowers.getInstance(username)
            1 -> fragment = FragmentFollowing.getInstance(username)
        }
        return fragment as Fragment
    }
}