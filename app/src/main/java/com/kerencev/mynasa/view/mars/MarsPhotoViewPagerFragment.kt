package com.kerencev.mynasa.view.mars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.kerencev.mynasa.R
import com.kerencev.mynasa.databinding.FragmentMarsPhotoViewPagerBinding
import com.kerencev.mynasa.view.earth.EarthFragment
import com.kerencev.mynasa.view.earth.OnImageClick
import com.kerencev.mynasa.view.photo.PhotoFragment

const val BUNDLE_KEY_LIST_IMAGE = "BUNDLE_KEY_LIST_IMAGE"

class MarsPhotoViewPagerFragment : Fragment() {
    private var _binding: FragmentMarsPhotoViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarsPhotoViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listOfImageUrl = arguments?.getStringArrayList(BUNDLE_KEY_LIST_IMAGE)
        var listOfFragment: List<PhotoFragment>? = null
        listOfImageUrl?.let { listOfFragment = getPhotoFragments(it) }
        listOfFragment?.let {
            val adapter = MarsPhotoViewPagerAdapter(
                fragment = this,
                data = it,
            )
            binding.viewPager.adapter = adapter

            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                binding.tv.text = "$position из ${listOfImageUrl?.size}"
            }.attach()
        }
    }

    private fun getPhotoFragments(listOfImage: java.util.ArrayList<String>): List<PhotoFragment> {
        val list: ArrayList<PhotoFragment> = ArrayList()
        listOfImage.forEach { imageUrl ->
            list.add(PhotoFragment.newInstance(imageUrl))
        }
        return list
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(listOfImageUrl: ArrayList<String>): MarsPhotoViewPagerFragment {
            return MarsPhotoViewPagerFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(BUNDLE_KEY_LIST_IMAGE, listOfImageUrl)
                }
            }
        }
    }
}