package com.kerencev.mynasa.model.repository

import com.kerencev.mynasa.BuildConfig
import com.kerencev.mynasa.data.retrofit.RetrofitCallBack
import com.kerencev.mynasa.data.retrofit.NasaAPI
import com.kerencev.mynasa.data.retrofit.entities.dates.DatesEarthPhotosResponse
import com.kerencev.mynasa.data.retrofit.entities.photo.EarthPhotoDataResponse
import com.kerencev.mynasa.data.retrofit.entities.pictureoftheday.PictureOfTheDayResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class RepositoryImpl : Repository {

    override fun getPictureOfTheDayApi(): PictureOfTheDayResponseData? {
        return try {
            NasaAPI.create().getPictureOfTheDay(BuildConfig.NASA_API_KEY).execute().body()
        } catch (e: IOException) {
            null
        }
    }

    override fun getPictureByDateApi(
        date: String,
        callBack: RetrofitCallBack<PictureOfTheDayResponseData>
    ) {
        NasaAPI.create().getPictureOfTheDayByDate(BuildConfig.NASA_API_KEY, date)
            .enqueue(object : Callback<PictureOfTheDayResponseData> {
                override fun onResponse(
                    call: Call<PictureOfTheDayResponseData>,
                    response: Response<PictureOfTheDayResponseData>
                ) {
                    callBack.response(response.body())
                }

                override fun onFailure(call: Call<PictureOfTheDayResponseData>, t: Throwable) {
                    callBack.response(null)
                }
            })

    }

    override fun getEarthPhotosDates(): DatesEarthPhotosResponse? {
        return NasaAPI.create().getEarthPhotosDates(BuildConfig.NASA_API_KEY).execute().body()
    }

    override fun getEarthPhotosData(date: String): EarthPhotoDataResponse? {
        return NasaAPI.create().getEarthPhotoData(date, BuildConfig.NASA_API_KEY).execute().body()
    }
}