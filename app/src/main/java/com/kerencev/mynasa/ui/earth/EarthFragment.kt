package com.kerencev.mynasa.ui.earth

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.transition.ChangeBounds
import androidx.transition.Slide
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import coil.load
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.kerencev.mynasa.BuildConfig
import com.kerencev.mynasa.R
import com.kerencev.mynasa.data.retrofit.entities.photo.EarthPhotoDataResponse
import com.kerencev.mynasa.databinding.FragmentEarthBinding
import com.kerencev.mynasa.model.helpers.MyDate
import com.kerencev.mynasa.ui.main.AppState
import org.koin.androidx.viewmodel.ext.android.viewModel

const val BUNDLE_KEY_DATE = "BUNDLE_KEY_DATE"

interface ViewPagerHandler {
    fun onImageClick(imageUrl: String)
}

class EarthFragment(private val viewPagerHandler: ViewPagerHandler) : Fragment() {
    private val viewModel: EarthViewModel by viewModel()
    private var _binding: FragmentEarthBinding? = null
    private val binding get() = _binding!!
    private var date: String? = null
    private lateinit var currentImageUrl: String

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

        val observer = Observer<AppState> { renderData(it) }
        viewModel.earthPhotoData.observe(viewLifecycleOwner, observer)
        date?.let { viewModel.getEarthPhotoData(it) }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                val data = appState.data as EarthPhotoDataResponse
                animateContent(duration = 300)
                binding.progressBar.visibility = View.GONE
                loadImage(data[0].image)
                renderChipGroup(data)
                binding.imgPhotoEarth.setOnClickListener {
                    viewPagerHandler.onImageClick(currentImageUrl)
                }
            }
            is AppState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.progressBar.visibility = View.GONE
                showSnackBarError()
            }
        }
    }

    private fun animateContent(duration: Long) {
        val animateTransition = TransitionSet()
        animateTransition.duration = duration
        animateTransition.ordering = TransitionSet.ORDERING_TOGETHER
        animateTransition.addTransition(Slide(Gravity.TOP))
        animateTransition.addTransition(ChangeBounds())
        TransitionManager.beginDelayedTransition(binding.main, animateTransition)
    }

    private fun loadImage(image: String) {
        val url = "https://api.nasa.gov/EPIC/archive/natural/" +
                date?.replace("-", "/", true) +
                "/png/" +
                image +
                ".png?api_key=${BuildConfig.NASA_API_KEY}"
        currentImageUrl = url
        binding.imgPhotoEarth.load(url) {
            placeholder(R.drawable.earth_place_holder)
            error(R.drawable.error)
        }
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
                loadImage(listOfPhotos[checkedId])
            }
        })
    }

    private fun showSnackBarError() {
        Snackbar
            .make(
                binding.main,
                R.string.data_could_not_be_retrieved_check_your_internet_connection,
                Snackbar.LENGTH_LONG
            )
            .setAction(R.string.reload) { date?.let { viewModel.getEarthPhotoData(it) } }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(date: String, onItemClick: ViewPagerHandler): EarthFragment {
            val bundle = Bundle()
            bundle.putString(BUNDLE_KEY_DATE, date)
            val fragment = EarthFragment(onItemClick)
            fragment.arguments = bundle
            return fragment
        }
    }
}