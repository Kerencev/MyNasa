package com.kerencev.mynasa.model.repository

import com.kerencev.mynasa.BuildConfig
import com.kerencev.mynasa.data.retrofit.NasaAPI
import com.kerencev.mynasa.data.retrofit.entities.PictureOfTheDayResponseData
import java.io.IOException

class RepositoryImpl : Repository {

    override fun getPictureOfTheDayApi(): PictureOfTheDayResponseData? {
        return try {
            NasaAPI.create().getPictureOfTheDay(BuildConfig.NASA_API_KEY).execute().body()
        } catch (e: IOException) {
            null
        }
    }

    override fun getPictureByDateApi(date: String): PictureOfTheDayResponseData? {
        return try {
            NasaAPI.create().getPictureOfTheDayByDate(BuildConfig.NASA_API_KEY, date).execute()
                .body()
        } catch (e: IOException) {
            null
        }
    }
}