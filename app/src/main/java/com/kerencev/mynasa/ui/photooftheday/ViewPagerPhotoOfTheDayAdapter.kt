package com.kerencev.mynasa.ui.photooftheday

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerPhotoOfTheDayAdapter(fragment: Fragment, val data: List<PhotoOfTheDayFragment>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return data.size
    }

    override fun createFragment(position: Int): Fragment {
        return data[position]
    }
}