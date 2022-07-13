package com.kerencev.mynasa.view.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.kerencev.mynasa.R
import com.kerencev.mynasa.databinding.FragmentPhotoBinding
import com.kerencev.mynasa.view.main.BottomNavigationHandler


const val BUNDLE_KEY_IMAGE_URL = "BUNDLE_KEY_IMAGE_URL"

class PhotoFragment : Fragment() {
    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!
    private var mainActivity: BottomNavigationHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = (activity as? BottomNavigationHandler)
        mainActivity?.isShowBottomNavigation(false)
        mainActivity?.isShowSystemBar(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageUrl = arguments?.getString(BUNDLE_KEY_IMAGE_URL)
        imageUrl?.let {
            binding.image.load(imageUrl) {
                placeholder(R.drawable.earth_place_holder)
                error(R.drawable.error)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mainActivity?.isShowBottomNavigation(true)
        mainActivity?.isShowSystemBar(true)
    }

    companion object {
        fun newInstance(imageUrl: String): PhotoFragment {
            return PhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(BUNDLE_KEY_IMAGE_URL, imageUrl)
                }
            }
        }
    }
}