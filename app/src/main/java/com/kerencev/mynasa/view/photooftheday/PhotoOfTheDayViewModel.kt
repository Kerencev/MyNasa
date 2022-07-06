package com.kerencev.mynasa.view.photooftheday

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerencev.mynasa.data.retrofit.RetrofitCallBack
import com.kerencev.mynasa.data.retrofit.entities.pictureoftheday.PictureOfTheDayResponseData
import com.kerencev.mynasa.model.repository.Repository
import com.kerencev.mynasa.view.main.AppState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotoOfTheDayViewModel(private val repository: Repository) : ViewModel() {
    private val _pictureOfTheDayData = MutableLiveData<AppState>()
    val pictureOfTheDayData: LiveData<AppState> get() = _pictureOfTheDayData

    fun getPictureByDate(date: String) {
        _pictureOfTheDayData.value = AppState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPictureByDateApi(date, object :
                RetrofitCallBack<PictureOfTheDayResponseData> {
                override fun response(data: PictureOfTheDayResponseData?) {
                    when (data) {
                        null -> {
                            _pictureOfTheDayData.postValue(AppState.Error)
                        }
                        else -> {
                            _pictureOfTheDayData.postValue(AppState.Success(data))
                        }
                    }
                }
            })
        }
    }
}