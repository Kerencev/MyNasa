package com.kerencev.mynasa.view.mars

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kerencev.mynasa.view.photo.PhotoFragment

class MarsPhotoViewPagerAdapter(fragment: Fragment, val data: List<PhotoFragment>): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return data.size
    }

    override fun createFragment(position: Int): Fragment {
        return data[position]
    }
}