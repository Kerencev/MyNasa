package com.kerencev.mynasa.ui.mars

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.transition.ArcMotion
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
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
        animateRoverImage()
        val lastDateObserver = Observer<String?> { date ->
            when (date) {
                null -> {
                    showSnackBar()
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

    private fun animateRoverImage() {
        Handler(Looper.getMainLooper()).postDelayed({
            val changeBounds = ChangeBounds()
            changeBounds.setPathMotion(ArcMotion())
            changeBounds.duration = 3000
            TransitionManager.beginDelayedTransition(
                binding.main,
                changeBounds
            )
            val params = binding.imgRover.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.CENTER or Gravity.END
            binding.imgRover.layoutParams = params
        }, 10)
    }

    private fun showSnackBar() {
        Snackbar
            .make(
                binding.main,
                R.string.data_could_not_be_retrieved_check_your_internet_connection,
                Snackbar.LENGTH_LONG
            )
            .setAction(R.string.reload) { viewModel.getLastDate() }
            .show()
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