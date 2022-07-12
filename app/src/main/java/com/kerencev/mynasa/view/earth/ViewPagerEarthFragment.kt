package com.kerencev.mynasa.view.earth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayoutMediator
import com.kerencev.mynasa.R
import com.kerencev.mynasa.data.retrofit.entities.dates.DatesEarthPhotosResponse
import com.kerencev.mynasa.databinding.ViewPagerPhotoOfTheDayBinding
import com.kerencev.mynasa.view.animation.DepthPageTransformer
import com.kerencev.mynasa.view.animation.ZoomOutPageTransformer
import com.kerencev.mynasa.view.photo.PhotoFragment
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
        viewPager.setPageTransformer(DepthPageTransformer())
    }

    private fun initAdapter(datesEarthPhotosResponse: DatesEarthPhotosResponse?) = with(binding) {
        if (datesEarthPhotosResponse == null || datesEarthPhotosResponse.isEmpty()) {
            // TODO Показать пользователю ошибку
            return
        }
        val adapter = ViewPagerEarthAdapter(
            fragment = this@ViewPagerEarthFragment,
            data = getEarthFragments(datesEarthPhotosResponse, 20),
        )
        viewPager.adapter = adapter
        bindTabLayout(datesEarthPhotosResponse)
    }

    private fun getEarthFragments(data: DatesEarthPhotosResponse, count: Int): List<EarthFragment> {
        val list: ArrayList<EarthFragment> = ArrayList()
        for (i in 0 until count) {
            data[i]?.date?.let { date ->
                list.add(EarthFragment.newInstance(date, object : OnImageClick {
                    override fun onClick(imageUrl: String) {
                        parentFragmentManager.beginTransaction()
                            .hide(this@ViewPagerEarthFragment)
                            .add(R.id.fragment_container, PhotoFragment.newInstance(imageUrl))
                            .addToBackStack(null)
                            .commitAllowingStateLoss()
                    }
                }))
            }
        }
        return list
    }

    private fun bindTabLayout(datesEarthPhotosResponse: DatesEarthPhotosResponse) = with(binding) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = datesEarthPhotosResponse[position]?.date.toString()
        }
    }.attach()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}