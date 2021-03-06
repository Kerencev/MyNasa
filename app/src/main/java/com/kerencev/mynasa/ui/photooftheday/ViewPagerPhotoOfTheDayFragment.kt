package com.kerencev.mynasa.ui.photooftheday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kerencev.mynasa.R
import com.kerencev.mynasa.databinding.ViewPagerPhotoOfTheDayBinding
import com.kerencev.mynasa.model.helpers.MyDate
import com.kerencev.mynasa.ui.animation.ZoomOutPageTransformer
import com.kerencev.mynasa.ui.earth.ViewPagerHandler
import com.kerencev.mynasa.ui.photo.PhotoFragment

class ViewPagerPhotoOfTheDayFragment(private val viewModel: PhotoOfTheDayViewModel) : Fragment() {
    private var _binding: ViewPagerPhotoOfTheDayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ViewPagerPhotoOfTheDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        viewPager.adapter = ViewPagerPhotoOfTheDayAdapter(
            fragment = this@ViewPagerPhotoOfTheDayFragment,
            data = getPhotoOfTheDayFragmentList()
        )
        viewPager.setPageTransformer(ZoomOutPageTransformer())
        bindTabLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getPhotoOfTheDayFragmentList(): List<PhotoOfTheDayFragment> {
        val result: ArrayList<PhotoOfTheDayFragment> = ArrayList()
        for (i in 0..2) {
            val date = if (i > 0) MyDate.getPastDays(i.toLong()) else null
            result.add(
                PhotoOfTheDayFragment.newInstance(
                    viewModel,
                    date,
                    object : ViewPagerHandler {
                        override fun onImageClick(imageUrl: String) {
                            parentFragmentManager.beginTransaction()
                                .hide(this@ViewPagerPhotoOfTheDayFragment)
                                .add(R.id.fragment_container, PhotoFragment.newInstance(imageUrl))
                                .addToBackStack(null)
                                .commitAllowingStateLoss()
                        }
                    })
            )
        }
        return result
    }

    private fun bindTabLayout() = with(binding) {
        TabLayoutMediator(
            tabLayout,
            viewPager,
            object : TabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    tab.text = when (position) {
                        0 -> {
                            resources.getString(R.string.today)
                        }
                        1 -> {
                            resources.getString(R.string.yesterday)
                        }
                        2 -> {
                            resources.getString(R.string.day_before_yesterday)
                        }
                        else -> ""
                    }
                }
            }).attach()
    }
}