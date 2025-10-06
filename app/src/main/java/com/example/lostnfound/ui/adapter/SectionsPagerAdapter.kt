package com.example.lostnfound.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.lostnfound.ui.home.FoundListFragment
import com.example.lostnfound.ui.home.LostListFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2 // Karena ada 2 tab: Lost dan Found
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = LostListFragment()
            1 -> fragment = FoundListFragment()
        }
        return fragment as Fragment
    }
}