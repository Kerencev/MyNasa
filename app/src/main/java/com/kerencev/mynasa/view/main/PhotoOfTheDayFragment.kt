package com.kerencev.mynasa.view.main

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
import com.kerencev.mynasa.databinding.FragmentPhotoOfTheDayBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

const val BUNDLE_DATE_KEY = "BUNDLE_DATE_KEY"

class MainFragment : Fragment() {
    private val viewModel: PhotoOfTheDayViewModel by viewModel()
    private var _binding: FragmentPhotoOfTheDayBinding? = null
    private val binding get() = _binding!!
    private var date: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoOfTheDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        date = arguments?.getString(BUNDLE_DATE_KEY)
        val pictureOfTheDayObserver = Observer<AppState> { renderData(it) }
        viewModel.pictureOfTheDayData.observe(viewLifecycleOwner, pictureOfTheDayObserver)
        date?.let { viewModel.getPictureByDate(it) }

        inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.input.text.toString()}")
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderData(appState: AppState) = with(binding) {
        when (appState) {
            is AppState.Success -> {
                setContent(appState.pictureOfTheDayData)
            }
            is AppState.Loading -> {
                progressBar.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                showSnackBarError()
            }
        }
    }

    private fun setContent(pictureOfTheDayResponseData: PictureOfTheDayResponseData) =
        with(binding) {
            progressBar.visibility = View.GONE
            imgPhotoDay.load(pictureOfTheDayResponseData.hdurl) {
                placeholder(R.drawable.nasa)
                error(R.drawable.error)
            }
            tvPhotoDay.text = pictureOfTheDayResponseData.explanation
        }

    private fun showSnackBarError() {
        binding.progressBar.visibility = View.GONE
        Snackbar
            .make(
                binding.main,
                R.string.data_could_not_be_retrieved_check_your_internet_connection,
                Snackbar.LENGTH_LONG
            )
            .setAction(R.string.reload) { date?.let { viewModel.getPictureByDate(it) } }
            .show()
    }

    companion object {
        fun newInstance(date: String): MainFragment {
            val bundle = Bundle()
            bundle.putString(BUNDLE_DATE_KEY, date)
            val fragment = MainFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}