package com.kerencev.mynasa.view.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kerencev.mynasa.model.helpers.MyDate
import com.kerencev.mynasa.view.main.MainFragment

class ViewPagerPhotoOfTheDayAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    private val fragments = arrayOf(
        MainFragment.newInstance(MyDate.getPastDays(0)),
        MainFragment.newInstance(MyDate.getPastDays(1)),
        MainFragment.newInstance(MyDate.getPastDays(2))
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}