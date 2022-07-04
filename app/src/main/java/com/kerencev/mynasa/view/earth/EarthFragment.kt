package com.kerencev.mynasa.view.earth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import coil.load
import com.kerencev.mynasa.BuildConfig
import com.kerencev.mynasa.data.retrofit.entities.photo.EarthPhotoDataResponse
import com.kerencev.mynasa.databinding.FragmentEarthBinding
import com.kerencev.mynasa.databinding.FragmentPhotoOfTheDayBinding
import com.kerencev.mynasa.view.photooftheday.BUNDLE_DATE_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel

const val BUNDLE_KEY_DATE = "BUNDLE_KEY_DATE"

class EarthFragment : Fragment() {
    private val viewModel: EarthViewModel by viewModel()
    private var _binding: FragmentEarthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEarthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val date = arguments?.getString(BUNDLE_KEY_DATE)

        val observer = Observer<EarthPhotoDataResponse?> { getImage(it, date) }
        viewModel.earthPhotoData.observe(viewLifecycleOwner, observer)
        date?.let { viewModel.getEarthPhotoData(date) }
    }

    private fun getImage(data: EarthPhotoDataResponse, date: String?) {
        val url = "https://api.nasa.gov/EPIC/archive/natural/" +
                date?.replace("-", "/", true) +
                "/png/" +
                "${data[0].image}" +
                ".png?api_key=${BuildConfig.NASA_API_KEY}"
        binding.imgPhotoEarth.load(url)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(date: String): EarthFragment {
            val bundle = Bundle()
            bundle.putString(BUNDLE_KEY_DATE, date)
            val fragment = EarthFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}