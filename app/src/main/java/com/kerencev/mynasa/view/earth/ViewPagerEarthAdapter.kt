package com.kerencev.mynasa.view.earth

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kerencev.mynasa.data.retrofit.entities.dates.DatesEarthPhotosResponse

class ViewPagerEarthAdapter(fragment: Fragment, val data: List<EarthFragment>) :
    FragmentStateAdapter(fragment) {

    private val fragments: ArrayList<EarthFragment> = ArrayList()

    override fun getItemCount(): Int {
        return data.size
    }

    override fun createFragment(position: Int): Fragment {
        return data[position]
    }

    fun setData(datesEarthPhotosResponse: DatesEarthPhotosResponse) {
        for (i in 0 until  20) {
            datesEarthPhotosResponse[i]?.date?.let {
                fragments.add(EarthFragment.newInstance(it))
            }
        }
    }
}