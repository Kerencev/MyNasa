package com.kerencev.mynasa.model.repository

import com.kerencev.mynasa.BuildConfig
import com.kerencev.mynasa.data.retrofit.NasaAPI
import com.kerencev.mynasa.data.retrofit.RetrofitCallBack
import com.kerencev.mynasa.data.retrofit.entities.dates.DatesEarthPhotosResponse
import com.kerencev.mynasa.data.retrofit.entities.mars.rovermanifest.MarsRoverManifestResponse
import com.kerencev.mynasa.data.retrofit.entities.mars.roverphotos.RoverPhotosResponse
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

    override fun getEarthPhotosDates(callBack: RetrofitCallBack<DatesEarthPhotosResponse>) {
        NasaAPI.create().getEarthPhotosDates(BuildConfig.NASA_API_KEY)
            .enqueue(object : Callback<DatesEarthPhotosResponse> {
                override fun onResponse(
                    call: Call<DatesEarthPhotosResponse>,
                    response: Response<DatesEarthPhotosResponse>
                ) {
                    callBack.response(response.body())
                }

                override fun onFailure(call: Call<DatesEarthPhotosResponse>, t: Throwable) {
                    callBack.response(null)
                }
            })
    }

    override fun getEarthPhotosData(
        date: String,
        callBack: RetrofitCallBack<EarthPhotoDataResponse>
    ) {
        NasaAPI.create().getEarthPhotoData(date, BuildConfig.NASA_API_KEY)
            .enqueue(object : Callback<EarthPhotoDataResponse> {
                override fun onResponse(
                    call: Call<EarthPhotoDataResponse>,
                    response: Response<EarthPhotoDataResponse>
                ) {
                    callBack.response(response.body())
                }

                override fun onFailure(call: Call<EarthPhotoDataResponse>, t: Throwable) {
                    callBack.response(null)
                }
            })
    }

    override fun getMarsRoverManifest(callBack: RetrofitCallBack<MarsRoverManifestResponse>) {
        NasaAPI.create().getRoverManifest(BuildConfig.NASA_API_KEY)
            .enqueue(object : Callback<MarsRoverManifestResponse> {
                override fun onResponse(
                    call: Call<MarsRoverManifestResponse>,
                    response: Response<MarsRoverManifestResponse>
                ) {
                    callBack.response(response.body())
                }

                override fun onFailure(call: Call<MarsRoverManifestResponse>, t: Throwable) {
                    callBack.response(null)
                }

            })
    }

    override fun getLastMarsPhotos(date: String, callBack: RetrofitCallBack<RoverPhotosResponse>) {
        NasaAPI.create().getRoverPhotos(date, BuildConfig.NASA_API_KEY)
            .enqueue(object : Callback<RoverPhotosResponse> {
                override fun onResponse(
                    call: Call<RoverPhotosResponse>,
                    response: Response<RoverPhotosResponse>
                ) {
                    callBack.response(response.body())
                }

                override fun onFailure(call: Call<RoverPhotosResponse>, t: Throwable) {
                    callBack.response(null)
                }
            })
    }
}