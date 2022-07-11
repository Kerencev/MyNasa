package com.kerencev.mynasa.view.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.davemorrissey.labs.subscaleview.ImageSource
import com.kerencev.mynasa.databinding.FragmentPhotoBinding

const val BUNDLE_IMAGE_URL_KEY = "BUNDLE_IMAGE_URL_KEY"

class PhotoScaleFragment : Fragment() {
    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!

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
        val imageUrl = arguments?.getString(BUNDLE_IMAGE_URL_KEY)
        imageUrl?.let { binding.imageView.setImage(ImageSource.uri(it)) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(imageUrl: String): PhotoScaleFragment {
            val bundle = Bundle()
            bundle.putString(BUNDLE_IMAGE_URL_KEY, imageUrl)
            val fragment = PhotoScaleFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}