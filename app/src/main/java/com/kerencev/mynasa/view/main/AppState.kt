package com.kerencev.mynasa.view.main

import com.kerencev.mynasa.data.retrofit.entities.pictureoftheday.PictureOfTheDayResponseData

sealed class AppState {
    data class Success(val pictureOfTheDayData: PictureOfTheDayResponseData) : AppState()
    object Error : AppState()
    object Loading : AppState()
}
