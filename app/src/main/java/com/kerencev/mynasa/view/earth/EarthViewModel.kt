package com.kerencev.mynasa.view.earth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerencev.mynasa.data.retrofit.entities.dates.DatesEarthPhotosResponse
import com.kerencev.mynasa.data.retrofit.entities.photo.EarthPhotoDataResponse
import com.kerencev.mynasa.model.repository.Repository
import com.kerencev.mynasa.view.main.AppState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EarthViewModel(private val repository: Repository) : ViewModel() {
    private val _earthPhotosDatesData = MutableLiveData<DatesEarthPhotosResponse?>()
    val earthPhotosDatesData: LiveData<DatesEarthPhotosResponse?> get() = _earthPhotosDatesData

    private val _earthPhotoData = MutableLiveData<EarthPhotoDataResponse?>()
    val earthPhotoData: LiveData<EarthPhotoDataResponse?> get() = _earthPhotoData

    fun getEarthPhotosDates() {
        viewModelScope.launch(Dispatchers.IO) {
            val dates = repository.getEarthPhotosDates()
            _earthPhotosDatesData.postValue(dates)
        }
    }

    fun getEarthPhotoData(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val dto = repository.getEarthPhotosData(date)
            _earthPhotoData.postValue(dto)
        }
    }
}