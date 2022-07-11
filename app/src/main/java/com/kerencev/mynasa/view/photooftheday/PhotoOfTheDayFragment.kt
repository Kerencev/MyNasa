package com.kerencev.mynasa.view.photooftheday

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.transition.*
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.kerencev.mynasa.R
import com.kerencev.mynasa.data.retrofit.entities.pictureoftheday.PictureOfTheDayResponseData
import com.kerencev.mynasa.databinding.FragmentPhotoOfTheDayBinding
import com.kerencev.mynasa.view.main.AppState
import org.koin.androidx.viewmodel.ext.android.viewModel


const val BUNDLE_DATE_KEY = "BUNDLE_DATE_KEY"

class PhotoOfTheDayFragment : Fragment() {
    private val viewModel: PhotoOfTheDayViewModel by viewModel()
    private var _binding: FragmentPhotoOfTheDayBinding? = null
    private val binding get() = _binding!!
    private var date: String? = null
    private var tapFlag = false

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
            is AppState.Success<*> -> {
                setContent(appState.data as PictureOfTheDayResponseData)
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
            animateAllContent() // Custom Animation
            progressBar.visibility = View.GONE
            imgPhotoDay.load(pictureOfTheDayResponseData.hdurl)
            tvPhotoDay.text = pictureOfTheDayResponseData.explanation
            imgPhotoDay.setOnClickListener {
                zoomImage(it)
            }
            tvPhotoDay.setOnClickListener {
                moveToTop()
            }
        }

    private fun moveToTop() = with(binding) {
        tapFlag = !tapFlag
        val params = scroll.layoutParams as ConstraintLayout.LayoutParams
        TransitionManager.beginDelayedTransition(binding.main)
        when (tapFlag) {
            true -> {
                params.topToBottom = R.id.inputLayout
                imgPhotoDay.visibility = View.GONE
            }
            false -> {
                params.topToBottom = R.id.img_photo_day
                imgPhotoDay.visibility = View.VISIBLE
            }
        }
        scroll.layoutParams = params
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

    private fun animateAllContent() {
        val animateTransition = TransitionSet()
        animateTransition.ordering = TransitionSet.ORDERING_TOGETHER
        animateTransition.addTransition(Slide(Gravity.TOP))
        animateTransition.addTransition(ChangeBounds())
        TransitionManager.beginDelayedTransition(binding.main, animateTransition)
    }

    private fun animateImageZoom() {
        val transitionSet = TransitionSet()
        transitionSet.addTransition(ChangeBounds())
        transitionSet.addTransition(ChangeImageTransform())
        TransitionManager.beginDelayedTransition(binding.main, transitionSet)
    }

    private fun zoomImage(view: View) = with(binding) {
        tapFlag = !tapFlag
        val params = view.layoutParams as ConstraintLayout.LayoutParams
        animateImageZoom()
        when (tapFlag) {
            true -> {
                params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
            }
            false -> {
                params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            }
        }
        view.layoutParams = params
    }

    companion object {
        fun newInstance(date: String): PhotoOfTheDayFragment {
            val bundle = Bundle()
            bundle.putString(BUNDLE_DATE_KEY, date)
            val fragment = PhotoOfTheDayFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}