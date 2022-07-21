package com.kerencev.mynasa.ui.mars

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.kerencev.mynasa.R
import com.kerencev.mynasa.data.retrofit.entities.mars.roverphotos.RoverPhotosResponse
import com.kerencev.mynasa.databinding.FragmentMarsBinding
import com.kerencev.mynasa.model.helpers.MyDate
import com.kerencev.mynasa.ui.main.AppState
import com.kerencev.mynasa.ui.photo.PhotoFragment

class MarsFragment(private val viewModel: MarsViewModel) : Fragment() {
    private var _binding: FragmentMarsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dto = viewModel.lastPhotosData.value
        dto?.let { renderData(it) }
    }

    private fun showDatePicker(year: Int, month: Int, day: Int) {
        DatePickerDialog(requireContext(), object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
                val date = MyDate.mapDateFromDatePickerToCorrectApiDate(year, month, day)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SplashScreenMarsFragment.newInstance(date))
                    .commitAllowingStateLoss()
            }
        }, year, month, day).show()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                val data = appState.data as RoverPhotosResponse
                if (data.photos.isEmpty()) {
                    showSnackBarEmptyData()
                    return
                }
                val dateForDatePicker = data.photos[0].earth_date.split("-")
                binding.actionSetDate.setOnClickListener {
                    showDatePicker(
                        dateForDatePicker[0].toInt(),
                        dateForDatePicker[1].toInt() - 1,
                        dateForDatePicker[2].toInt()
                    )
                }
                val adapter = MarsPhotoAdapter { imageUrl ->
                    parentFragmentManager.beginTransaction()
                        .hide(this)
                        .add(
                            R.id.fragment_container,
                            PhotoFragment.newInstance(imageUrl)
                        )
                        .addToBackStack(null)
                        .commitAllowingStateLoss()
                }
                binding.recycler.adapter = adapter
                adapter.setData(data.photos)
                binding.tvInfoDate.text = "Earth date: ${data.photos[0].earth_date}\n" +
                        "Sol: ${data.photos[0].sol}"
            }
            is AppState.Loading -> {}
            is AppState.Error -> {
                showSnackBarError()
            }
        }
    }

    private fun showSnackBarEmptyData() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        Snackbar
            .make(
                binding.root,
                resources.getString(R.string.there_are_no_photos_on_this_date),
                Snackbar.LENGTH_LONG
            )
            .setAction(resources.getString(R.string.select_date)) {
                showDatePicker(
                    year,
                    month,
                    day
                )
            }
            .show()
        binding.actionSetDate.setOnClickListener {
            showDatePicker(year, month, day)
        }
    }

    private fun showSnackBarError() {
        Snackbar
            .make(
                binding.root,
                resources.getString(R.string.data_could_not_be_retrieved_check_your_internet_connection),
                Snackbar.LENGTH_LONG
            )
            .setAction("Reload") { viewModel.getLastDate() }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}