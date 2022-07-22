package com.kerencev.mynasa.ui.photooftheday

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import com.kerencev.mynasa.R
import com.kerencev.mynasa.databinding.FragmentSplashScreenPhotoOfTheDayBinding
import com.kerencev.mynasa.model.helpers.MyDate
import com.kerencev.mynasa.ui.ViewBindingFragment
import com.kerencev.mynasa.ui.main.AppState
import com.kerencev.mynasa.ui.main.BottomNavigationHandler
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenPhotoOfTheDayFragment :
    ViewBindingFragment<FragmentSplashScreenPhotoOfTheDayBinding>(
        FragmentSplashScreenPhotoOfTheDayBinding::inflate
    ) {

    private val vieModel: PhotoOfTheDayViewModel by viewModel()
    private lateinit var timerForStars: CountDownTimer
    private lateinit var timerForTelescope: CountDownTimer
    private var flagForStars = false
    private var flagForTelescope = false
    private var mainActivity: BottomNavigationHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = (activity as? BottomNavigationHandler)
        mainActivity?.isShowBottomNavigation(false)
        mainActivity?.isShowSystemBar(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animateStarsImage()
        animateTelescopeImage()
        val observer = Observer<AppState> {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.to_left_in, R.animator.to_left_out)
                .replace(R.id.fragment_container, ViewPagerPhotoOfTheDayFragment(vieModel))
                .commitAllowingStateLoss()
        }
        vieModel.pictureOfTheDayData.observe(viewLifecycleOwner, observer)
        vieModel.getPictureByDate(MyDate.getPastDays(0))
    }

    private fun animateStarsImage() {
        val animationAlphaUp = AnimationUtils.loadAnimation(requireContext(), R.anim.alpha_up)
        val animationAlphaDown = AnimationUtils.loadAnimation(requireContext(), R.anim.alpha_down)
        timerForStars = object : CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                flagForStars = !flagForStars
                when (flagForStars) {
                    true -> {
                        binding.imgStars.startAnimation(animationAlphaUp)
                    }
                    false -> {
                        binding.imgStars.startAnimation(animationAlphaDown)
                    }
                }
            }

            override fun onFinish() {}
        }.start()
    }

    private fun animateTelescopeImage() {
        timerForTelescope = object : CountDownTimer(20000, 4000) {
            override fun onTick(millisUntilFinished: Long) {
                flagForTelescope = !flagForTelescope
                when (flagForTelescope) {
                    true -> {
                        binding.telescope.animate().rotation(-20f).setDuration(4000L).start()
                    }
                    false -> {
                        binding.telescope.animate().rotation(0f).setDuration(4000L).start()
                    }
                }
            }

            override fun onFinish() {}
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timerForStars.cancel()
        timerForTelescope.cancel()
        mainActivity?.isShowBottomNavigation(true)
        mainActivity?.isShowSystemBar(true)
    }
}