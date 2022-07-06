package com.kerencev.mynasa.view.earth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerencev.mynasa.data.retrofit.RetrofitCallBack
import com.kerencev.mynasa.data.retrofit.entities.dates.DatesEarthPhotosResponse
import com.kerencev.mynasa.data.retrofit.entities.photo.EarthPhotoDataResponse
import com.kerencev.mynasa.model.repository.Repository
import com.kerencev.mynasa.view.main.AppState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EarthViewModel(private val repository: Repository) : ViewModel() {
    private val _earthPhotosDatesData = MutableLiveData<DatesEarthPhotosResponse?>()
    val earthPhotosDatesData: LiveData<DatesEarthPhotosResponse?> get() = _earthPhotosDatesData

    private val _earthPhotoData = MutableLiveData<AppState>()
    val earthPhotoData: LiveData<AppState> get() = _earthPhotoData

    fun getEarthPhotosDates() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getEarthPhotosDates(object : RetrofitCallBack<DatesEarthPhotosResponse> {
                override fun response(data: DatesEarthPhotosResponse?) {
                    _earthPhotosDatesData.postValue(data)
                }
            })
        }
    }

    fun getEarthPhotoData(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getEarthPhotosData(date, object : RetrofitCallBack<EarthPhotoDataResponse> {
                override fun response(data: EarthPhotoDataResponse?) {
                    when (data) {
                        null -> _earthPhotoData.postValue(AppState.Error)
                        else -> _earthPhotoData.postValue(
                            AppState.Success<EarthPhotoDataResponse>(
                                data
                            )
                        )
                    }
                }
            })
        }
    }
}