package com.kerencev.mynasa.view.photooftheday

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kerencev.mynasa.model.helpers.MyDate
import com.kerencev.mynasa.view.earth.EarthFragment

class ViewPagerPhotoOfTheDayAdapter(fragment: Fragment, val data: List<PhotoOfTheDayFragment>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return data.size
    }

    override fun createFragment(position: Int): Fragment {
        return data[position]
    }
}