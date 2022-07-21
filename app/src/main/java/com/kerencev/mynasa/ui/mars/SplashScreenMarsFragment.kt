package com.kerencev.mynasa.ui.mars

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.os.postDelayed
import androidx.lifecycle.Observer
import androidx.transition.ArcMotion
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.kerencev.mynasa.R
import com.kerencev.mynasa.databinding.FragmentSplashScreenMarsBinding
import com.kerencev.mynasa.ui.ViewBindingFragment
import com.kerencev.mynasa.ui.main.AppState
import org.koin.androidx.viewmodel.ext.android.viewModel

const val BUNDLE_KEY_DATE_MARS = "BUNDLE_KEY_DATE_MARS"

class SplashScreenMarsFragment :
    ViewBindingFragment<FragmentSplashScreenMarsBinding>(FragmentSplashScreenMarsBinding::inflate) {

    private val viewModel: MarsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.imgMars.animate().setDuration(10000L).rotationY(360f).start()

        Handler(Looper.getMainLooper()).postDelayed({
            val changeBounds = ChangeBounds()
            changeBounds.setPathMotion(ArcMotion())
            changeBounds.duration = 3000
            TransitionManager.beginDelayedTransition(
                binding.main,
                changeBounds
            )
            val params = binding.imgMars.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.CENTER or Gravity.END
            binding.imgMars.layoutParams = params
        }, 10)

        val lastDateObserver = Observer<String?> { date ->
            when (date) {
                null -> {
                    navigateToMarsFragment()
                }
                else -> {
                    viewModel.getPhotosByDate(date)
                }
            }
        }
        viewModel.lastDateData.observe(viewLifecycleOwner, lastDateObserver)
        val lastPhotosObserver = Observer<AppState> {
            navigateToMarsFragment()
        }
        viewModel.lastPhotosData.observe(viewLifecycleOwner, lastPhotosObserver)

        when (val date = arguments?.getString(BUNDLE_KEY_DATE_MARS)) {
            null -> viewModel.getLastDate()
            else -> viewModel.getPhotosByDate(date)
        }
    }

    private fun navigateToMarsFragment() {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.animator.to_left_in, R.animator.to_left_out)
            .replace(R.id.fragment_container, MarsFragment(viewModel))
            .commitAllowingStateLoss()
    }

    companion object {
        fun newInstance(date: String): SplashScreenMarsFragment {
            return SplashScreenMarsFragment().apply {
                arguments = Bundle().apply {
                    putString(BUNDLE_KEY_DATE_MARS, date)
                }
            }
        }
    }
}