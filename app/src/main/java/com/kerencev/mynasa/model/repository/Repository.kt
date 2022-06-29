package com.kerencev.mynasa.model.repository

import com.kerencev.mynasa.data.retrofit.entities.PictureOfTheDayResponseData

interface Repository {
    fun getPictureOfTheDayApi(): PictureOfTheDayResponseData?
    fun getPictureByDateApi(date: String): PictureOfTheDayResponseData?
}