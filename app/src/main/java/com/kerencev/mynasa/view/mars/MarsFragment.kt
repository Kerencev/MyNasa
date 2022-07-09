package com.kerencev.mynasa.view.mars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.kerencev.mynasa.data.retrofit.entities.mars.roverphotos.RoverPhotosResponse
import com.kerencev.mynasa.databinding.FragmentMarsBinding
import com.kerencev.mynasa.view.main.AppState
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
                null -> {}//TODO Показать ошибку
                else -> viewModel.getLastPhotos(date)
            }
        }
        viewModel.lastDateData.observe(viewLifecycleOwner, lastDateObserver)

        val lastPhotosObserver = Observer<AppState> { renderData(it) }
        viewModel.lastPhotosData.observe(viewLifecycleOwner, lastPhotosObserver)

        viewModel.getLastDate()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                val data = appState.data as RoverPhotosResponse
                val adapter = MarsPhotoAdapter()
                binding.recycler.adapter = adapter
                adapter.setData(data.photos)
            }
            is AppState.Loading -> {}
            is AppState.Error -> {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}