package com.kerencev.mynasa.view.photooftheday

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kerencev.mynasa.model.helpers.MyDate

class ViewPagerPhotoOfTheDayAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    private val fragments = arrayOf(
        PhotoOfTheDayFragment.newInstance(MyDate.getPastDays(0)),
        PhotoOfTheDayFragment.newInstance(MyDate.getPastDays(1)),
        PhotoOfTheDayFragment.newInstance(MyDate.getPastDays(2))
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}