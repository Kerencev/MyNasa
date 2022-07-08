package com.kerencev.mynasa.view.mars

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerencev.mynasa.data.retrofit.RetrofitCallBack
import com.kerencev.mynasa.data.retrofit.entities.mars.MarsRoverManifestResponse
import com.kerencev.mynasa.model.repository.Repository
import com.kerencev.mynasa.view.main.AppState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MarsViewModel(private val repository: Repository) : ViewModel() {
    private val _lastDateData = MutableLiveData<String?>()
    val lastDateData: LiveData<String?> get() = _lastDateData

    private val _lastPhotosData = MutableLiveData<AppState>()
    val lastPhotosData: LiveData<AppState> get() = _lastPhotosData

    fun getLastPhotos(date: String) {

    }

    fun getLastDate() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMarsRoverManifest(object : RetrofitCallBack<MarsRoverManifestResponse> {
                override fun response(data: MarsRoverManifestResponse?) {
                    when (data) {
                        null -> _lastDateData.postValue(null)
                        else -> _lastDateData.postValue(data.photo_manifest.max_date)
                    }
                }
            })
        }
    }
}