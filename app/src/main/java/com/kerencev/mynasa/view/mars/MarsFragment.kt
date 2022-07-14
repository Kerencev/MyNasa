package com.kerencev.mynasa.view.mars

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.kerencev.mynasa.R
import com.kerencev.mynasa.data.retrofit.entities.mars.roverphotos.RoverPhotosResponse
import com.kerencev.mynasa.databinding.FragmentMarsBinding
import com.kerencev.mynasa.model.helpers.MyDate
import com.kerencev.mynasa.view.main.AppState
import com.kerencev.mynasa.view.photo.PhotoFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MarsFragment : Fragment() {
    private val viewModel: MarsViewModel by viewModel()
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

        val lastDateObserver = Observer<String?> { date ->
            when (date) {
                null -> {
                    showSnackBarError()
                }
                else -> viewModel.getPhotosByDate(date)
            }
        }
        viewModel.lastDateData.observe(viewLifecycleOwner, lastDateObserver)
        val lastPhotosObserver = Observer<AppState> { renderData(it) }
        viewModel.lastPhotosData.observe(viewLifecycleOwner, lastPhotosObserver)
        viewModel.getLastDate()
        binding.actionSetDate.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(requireContext(), object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
                val date = MyDate.mapDateFromDatePickerToCorrectApiDate(year, month, day)
                viewModel.getPhotosByDate(date)
            }
        }, year, month, day).show()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                showProgressBar(false)
                val data = appState.data as RoverPhotosResponse
                if (data.photos.isEmpty()) {
                    showSnackBarEmptyData()
                    return
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
            is AppState.Loading -> {
                showProgressBar(true)
            }
            is AppState.Error -> {
                showSnackBarError()
            }
        }
    }

    private fun showSnackBarEmptyData() {
        Snackbar
            .make(
                binding.root,
                resources.getString(R.string.there_are_no_photos_on_this_date),
                Snackbar.LENGTH_LONG
            )
            .setAction(resources.getString(R.string.select_date)) { showDatePicker() }
            .show()
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

    private fun showProgressBar(isShow: Boolean) {
        when (isShow) {
            true -> binding.progressBar.visibility = View.VISIBLE
            false -> binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}