package com.kerencev.mynasa.ui.photooftheday

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.text.toSpannable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.transition.ChangeBounds
import androidx.transition.Slide
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.kerencev.mynasa.R
import com.kerencev.mynasa.data.retrofit.entities.pictureoftheday.PictureOfTheDayResponseData
import com.kerencev.mynasa.databinding.FragmentPhotoOfTheDayBinding
import com.kerencev.mynasa.ui.earth.ViewPagerHandler
import com.kerencev.mynasa.ui.main.AppState
import org.koin.androidx.viewmodel.ext.android.viewModel

const val BUNDLE_DATE_KEY = "BUNDLE_DATE_KEY"

class PhotoOfTheDayFragment(private val viewPagerHandler: ViewPagerHandler) : Fragment() {
    private val viewModel: PhotoOfTheDayViewModel by viewModel()
    private var _binding: FragmentPhotoOfTheDayBinding? = null
    private val binding get() = _binding!!
    private var date: String? = null
    lateinit var spannableRainbow: SpannableString
    private var timer: CountDownTimer? = null
    private var matrixFlag = false

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
        timer?.cancel()
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
            imgPhotoDay.load(pictureOfTheDayResponseData.hdurl) {
                placeholder(R.drawable.nasa)
            }
            val textWithImageSpan = addImageSpan(pictureOfTheDayResponseData.explanation)
            tvPhotoDay.text = textWithImageSpan

            imgPhotoDay.setOnClickListener {
                viewPagerHandler.onImageClick(pictureOfTheDayResponseData.hdurl)
            }

            spannableRainbow = SpannableString(tvPhotoDay.text.toSpannable())
            rainbow(1)
            actionMatrix.setOnClickListener {
                matrixFlag = !matrixFlag
                when (matrixFlag) {
                    false -> {
                        imgUserNeo.setImageResource(R.drawable.user)
                        timer?.cancel()
                        tvPhotoDay.text = textWithImageSpan
                    }
                    true -> {
                        imgUserNeo.setImageResource(R.drawable.matrix)
                        timer?.start()
                    }
                }
            }
        }

    fun rainbow(i: Int = 1) {
        var currentCount = i
        timer = object : CountDownTimer(20000, 200) {
            override fun onTick(millisUntilFinished: Long) {
                colorText(currentCount)
                currentCount = if (++currentCount > 5) 1 else currentCount
            }

            override fun onFinish() {
                rainbow(currentCount)
            }
        }
    }

    private fun colorText(colorFirstNumber: Int) {
        binding.tvPhotoDay.setText(spannableRainbow, TextView.BufferType.SPANNABLE)
        spannableRainbow = binding.tvPhotoDay.text as SpannableString
        val map = mapOf(
            0 to ContextCompat.getColor(requireContext(), R.color.green),
            1 to ContextCompat.getColor(requireContext(), R.color.green1),
            2 to ContextCompat.getColor(requireContext(), R.color.green2),
            3 to ContextCompat.getColor(requireContext(), R.color.green3),
            4 to ContextCompat.getColor(requireContext(), R.color.green4),
            5 to ContextCompat.getColor(requireContext(), R.color.green4),
            6 to ContextCompat.getColor(requireContext(), R.color.green4),
        )
        val spans = spannableRainbow.getSpans(
            0, spannableRainbow.length,
            ForegroundColorSpan::class.java
        )
        for (span in spans) {
            spannableRainbow.removeSpan(span)
        }

        var colorNumber = colorFirstNumber
        for (i in 0 until binding.tvPhotoDay.text.length) {
            if (colorNumber == 5) colorNumber = 0 else colorNumber += 1
            spannableRainbow.setSpan(
                ForegroundColorSpan(map.getValue(colorNumber)),
                i, i + 1,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }
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

    private fun addImageSpan(text: String): SpannableString {
        val newText = " \n$text"
        val spannableString = SpannableString(newText)
        val bitmap =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_info_24)!!.toBitmap()
        spannableString.setSpan(
            ImageSpan(requireContext(), bitmap),
            0,
            1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    private fun animateAllContent() {
        val animateTransition = TransitionSet()
        animateTransition.ordering = TransitionSet.ORDERING_TOGETHER
        animateTransition.addTransition(Slide(Gravity.TOP))
        animateTransition.addTransition(ChangeBounds())
        TransitionManager.beginDelayedTransition(binding.main, animateTransition)
    }

    companion object {
        fun newInstance(date: String, viewPagerHandler: ViewPagerHandler): PhotoOfTheDayFragment {
            val bundle = Bundle()
            bundle.putString(BUNDLE_DATE_KEY, date)
            val fragment = PhotoOfTheDayFragment(viewPagerHandler)
            fragment.arguments = bundle
            return fragment
        }
    }
}