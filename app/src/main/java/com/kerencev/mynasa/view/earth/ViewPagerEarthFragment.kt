package com.kerencev.mynasa.view.earth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kerencev.mynasa.R
import com.kerencev.mynasa.data.retrofit.entities.dates.DatesEarthPhotosResponse
import com.kerencev.mynasa.databinding.ViewPagerPhotoOfTheDayBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ViewPagerEarthFragment : Fragment() {
    private val viewModel: EarthViewModel by viewModel()
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

        val observer = Observer<DatesEarthPhotosResponse?> { initAdapter(it) }
        viewModel.earthPhotosDatesData.observe(viewLifecycleOwner, observer)
        viewModel.getEarthPhotosDates()
    }

    private fun initAdapter(datesEarthPhotosResponse: DatesEarthPhotosResponse?) = with(binding) {
        if (datesEarthPhotosResponse == null || datesEarthPhotosResponse.isEmpty()) {
            // TODO Показать пользователю ошибку
            return
        }
        val list: ArrayList<EarthFragment> = ArrayList()
        for (i in 0 until  20) {
            datesEarthPhotosResponse[i]?.date?.let {
                list.add(EarthFragment.newInstance(it))
            }
        }
        val adapter = ViewPagerEarthAdapter(this@ViewPagerEarthFragment, list)
        viewPager.adapter = adapter
        bindTabLayout(datesEarthPhotosResponse)
    }

    private fun bindTabLayout(datesEarthPhotosResponse: DatesEarthPhotosResponse) = with(binding) {
        TabLayoutMediator(
            tabLayout,
            viewPager,
            object : TabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    tab.text = when (position) {
                        0 -> {
                            datesEarthPhotosResponse[0]?.date.toString()
                        }
                        1 -> {
                            datesEarthPhotosResponse[1]?.date.toString()
                        }
                        2 -> {
                            datesEarthPhotosResponse[2]?.date.toString()
                        }
                        3 -> {
                            datesEarthPhotosResponse[3]?.date.toString()
                        }
                        4 -> {
                            datesEarthPhotosResponse[4]?.date.toString()
                        }
                        5 -> {
                            datesEarthPhotosResponse[5]?.date.toString()
                        }
                        6 -> {
                            datesEarthPhotosResponse[6]?.date.toString()
                        }
                        7 -> {
                            datesEarthPhotosResponse[7]?.date.toString()
                        }
                        8 -> {
                            datesEarthPhotosResponse[8]?.date.toString()
                        }
                        9 -> {
                            datesEarthPhotosResponse[9]?.date.toString()
                        }
                        10 -> {
                            datesEarthPhotosResponse[10]?.date.toString()
                        }
                        11 -> {
                            datesEarthPhotosResponse[11]?.date.toString()
                        }
                        12 -> {
                            datesEarthPhotosResponse[12]?.date.toString()
                        }
                        13 -> {
                            datesEarthPhotosResponse[13]?.date.toString()
                        }
                        14 -> {
                            datesEarthPhotosResponse[14]?.date.toString()
                        }
                        15 -> {
                            datesEarthPhotosResponse[15]?.date.toString()
                        }
                        16 -> {
                            datesEarthPhotosResponse[16]?.date.toString()
                        }
                        17 -> {
                            datesEarthPhotosResponse[17]?.date.toString()
                        }
                        18 -> {
                            datesEarthPhotosResponse[18]?.date.toString()
                        }
                        19 -> {
                            datesEarthPhotosResponse[19]?.date.toString()
                        }
                        else -> ""
                    }
                }
            }).attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}