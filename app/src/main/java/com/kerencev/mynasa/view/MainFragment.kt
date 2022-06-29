package com.kerencev.mynasa.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.kerencev.mynasa.R
import com.kerencev.mynasa.data.retrofit.entities.PictureOfTheDayResponseData
import com.kerencev.mynasa.databinding.FragmentMainBinding
import com.kerencev.mynasa.model.helpers.MyDate
import com.kerencev.mynasa.viewmodel.AppState
import com.kerencev.mynasa.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {
    private val viewModel: MainViewModel by viewModel()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var snackBarLoading: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        snackBarLoading =
            Snackbar.make(binding.main, R.string.loading_data, Snackbar.LENGTH_INDEFINITE)

        val pictureOfTheDayObserver = Observer<AppState> { renderData(it) }
        viewModel.pictureOfTheDayData.observe(viewLifecycleOwner, pictureOfTheDayObserver)
        viewModel.getPictureOfTheDay()

        inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.input.text.toString()}")
            })
        }
        setChipGroupClicks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setChipGroupClicks() = with(binding) {
        chipDayBeforeYesterday.setOnClickListener {
            it.isClickable = false
            viewModel.getPictureByDate(MyDate.getPastDays(2))
        }
        chipYesterday.setOnClickListener {
            it.isClickable = false
            viewModel.getPictureByDate(MyDate.getPastDays(1))
        }
        chipToday.setOnClickListener {
            it.isClickable = false
            viewModel.getPictureOfTheDay()
        }
    }

    private fun renderData(appState: AppState) = with(binding) {
        when (appState) {
            is AppState.Success -> {
                setContent(appState.pictureOfTheDayData)
            }
            is AppState.Loading -> {
                snackBarLoading.show()
            }
            is AppState.Error -> {
                showSnackBarError()
            }
        }
    }

    private fun setContent(pictureOfTheDayResponseData: PictureOfTheDayResponseData) =
        with(binding) {
            snackBarLoading.dismiss()
            imgPhotoDay.load(getImgUrl(pictureOfTheDayResponseData)) {
                placeholder(R.drawable.nasa)
                error(R.drawable.error)
            }
            tvPhotoDay.text = pictureOfTheDayResponseData.explanation
        }

    private fun getImgUrl(pictureOfTheDayResponseData: PictureOfTheDayResponseData): String {
        return when (binding.checkboxHdImg.isChecked) {
            true -> pictureOfTheDayResponseData.hdurl
            false -> pictureOfTheDayResponseData.url
        }
    }

    private fun showSnackBarError() {
        Snackbar
            .make(
                binding.main,
                R.string.data_could_not_be_retrieved_check_your_internet_connection,
                Snackbar.LENGTH_LONG
            )
            .setAction(R.string.reload) { viewModel.getPictureOfTheDay() }
            .show()
    }
}