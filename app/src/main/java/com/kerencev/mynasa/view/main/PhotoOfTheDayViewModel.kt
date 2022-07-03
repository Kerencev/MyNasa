package com.kerencev.mynasa.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerencev.mynasa.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotoOfTheDayViewModel(private val repository: Repository) : ViewModel() {
    private val _pictureOfTheDayData = MutableLiveData<AppState>()
    val pictureOfTheDayData: LiveData<AppState> get() = _pictureOfTheDayData

    fun getPictureByDate(date: String) {
        _pictureOfTheDayData.value = AppState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.getPictureByDateApi(date)) {
                null -> _pictureOfTheDayData.postValue(AppState.Error)
                else -> _pictureOfTheDayData.postValue(AppState.Success(result))
            }
        }
    }
}