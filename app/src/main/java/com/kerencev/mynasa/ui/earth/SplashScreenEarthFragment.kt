package com.kerencev.mynasa.ui.earth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.transition.ArcMotion
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.kerencev.mynasa.R
import com.kerencev.mynasa.data.retrofit.entities.dates.DatesEarthPhotosResponse
import com.kerencev.mynasa.databinding.FragmentSplashScreenEarthBinding
import com.kerencev.mynasa.ui.ViewBindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenEarthFragment :
    ViewBindingFragment<FragmentSplashScreenEarthBinding>(FragmentSplashScreenEarthBinding::inflate) {

    private val viewModel: EarthViewModel by viewModel()
    private lateinit var handler: Handler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgEarth.animate().rotation(720f).setDuration(20000L).start()
        handler = Handler(Looper.getMainLooper())
        animateStars()
        animateComet()
        animateAstronaut()

        val observer = Observer<DatesEarthPhotosResponse?> {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ViewPagerEarthFragment(viewModel))
                .commitAllowingStateLoss()
        }
        viewModel.earthPhotosDatesData.observe(viewLifecycleOwner, observer)
        viewModel.getEarthPhotosDates()
    }

    private fun animateAstronaut() {
        handler.postDelayed({
            val changeBounds = ChangeBounds()
            changeBounds.setPathMotion(ArcMotion())
            changeBounds.duration = 5000
            TransitionManager.beginDelayedTransition(
                binding.main,
                changeBounds
            )
            val params = binding.imgAstronaut.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.TOP or Gravity.END
            binding.imgAstronaut.layoutParams = params
        }, 30)
    }

    private fun animateComet() {
        handler.postDelayed({
            val fade1 = Fade()
            fade1.duration = 5000L
            TransitionManager.beginDelayedTransition(binding.main, fade1)
            binding.imgComet.visibility = View.VISIBLE
        }, 20)
    }

    private fun animateStars() {
        handler.postDelayed({
            val fade = Fade()
            fade.duration = 500L
            TransitionManager.beginDelayedTransition(binding.main, fade)
            binding.imgStars.visibility = View.VISIBLE
        }, 10)
    }
}