package com.kerencev.mynasa.view.earth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import coil.load
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.kerencev.mynasa.BuildConfig
import com.kerencev.mynasa.data.retrofit.entities.photo.EarthPhotoDataResponse
import com.kerencev.mynasa.databinding.FragmentEarthBinding
import com.kerencev.mynasa.model.helpers.MyDate
import org.koin.androidx.viewmodel.ext.android.viewModel

const val BUNDLE_KEY_DATE = "BUNDLE_KEY_DATE"

class EarthFragment : Fragment() {
    private val viewModel: EarthViewModel by viewModel()
    private var _binding: FragmentEarthBinding? = null
    private val binding get() = _binding!!
    private var date: String? = null

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
        date = arguments?.getString(BUNDLE_KEY_DATE)

        val observer = Observer<EarthPhotoDataResponse?> {
            loadImage(it, date)
            renderChipGroup(it)
        }
        viewModel.earthPhotoData.observe(viewLifecycleOwner, observer)
        date?.let { viewModel.getEarthPhotoData(it) }
    }

    private fun loadImage(data: EarthPhotoDataResponse, date: String?) {
        val url = "https://api.nasa.gov/EPIC/archive/natural/" +
                date?.replace("-", "/", true) +
                "/png/" +
                "${data[0].image}" +
                ".png?api_key=${BuildConfig.NASA_API_KEY}"
        binding.imgPhotoEarth.load(url)
    }

    private fun renderChipGroup(data: EarthPhotoDataResponse) = with(binding) {
        val listOfDates = mutableListOf<String>()
        val listOfPhotos = mutableListOf<String>()
        data.forEach {
            listOfDates.add(it.date)
            listOfPhotos.add(it.image)
        }

        for (i in 0 until listOfDates.size) {
            val chip = Chip(chipGroup.context)
            chip.id = i
            chip.text = MyDate.getTime(listOfDates[i])
            chipGroup.addView(chip)
        }

        chipGroup.getChildAt(0).isSelected = true
        chipGroup.setOnCheckedChangeListener(object : ChipGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: ChipGroup?, checkedId: Int) {
                if (checkedId != 0) chipGroup.getChildAt(0).isSelected = false
                val url = "https://api.nasa.gov/EPIC/archive/natural/" +
                        date?.replace("-", "/", true) +
                        "/png/" +
                        listOfPhotos[checkedId] +
                        ".png?api_key=${BuildConfig.NASA_API_KEY}"
                binding.imgPhotoEarth.load(url)
            }
        })
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