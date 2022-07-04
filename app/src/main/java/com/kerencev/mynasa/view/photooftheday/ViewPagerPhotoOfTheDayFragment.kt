package com.kerencev.mynasa.view.photooftheday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kerencev.mynasa.R
import com.kerencev.mynasa.databinding.ViewPagerPhotoOfTheDayBinding

class ViewPagerPhotoOfTheDayFragment : Fragment() {
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
        viewPager.adapter = ViewPagerPhotoOfTheDayAdapter(this@ViewPagerPhotoOfTheDayFragment)
        bindTabLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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