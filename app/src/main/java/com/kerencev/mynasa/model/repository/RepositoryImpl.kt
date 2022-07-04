package com.kerencev.mynasa.model.repository

import com.kerencev.mynasa.BuildConfig
import com.kerencev.mynasa.data.retrofit.NasaAPI
import com.kerencev.mynasa.data.retrofit.entities.dates.DatesEarthPhotosResponse
import com.kerencev.mynasa.data.retrofit.entities.photo.EarthPhotoDataResponse
import com.kerencev.mynasa.data.retrofit.entities.pictureoftheday.PictureOfTheDayResponseData
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

    override fun getEarthPhotosDates(): DatesEarthPhotosResponse? {
        return NasaAPI.create().getEarthPhotosDates(BuildConfig.NASA_API_KEY).execute().body()
    }

    override fun getEarthPhotosData(date: String): EarthPhotoDataResponse? {
        return NasaAPI.create().getEarthPhotoData(date, BuildConfig.NASA_API_KEY).execute().body()
    }
}