package com.kerencev.mynasa.model.repository

import com.kerencev.mynasa.data.retrofit.RetrofitCallBack
import com.kerencev.mynasa.data.retrofit.entities.dates.DatesEarthPhotosResponse
import com.kerencev.mynasa.data.retrofit.entities.mars.MarsRoverManifestResponse
import com.kerencev.mynasa.data.retrofit.entities.photo.EarthPhotoDataResponse
import com.kerencev.mynasa.data.retrofit.entities.pictureoftheday.PictureOfTheDayResponseData

interface Repository {
    fun getPictureOfTheDayApi(): PictureOfTheDayResponseData?
    fun getPictureByDateApi(date: String, callBack: RetrofitCallBack<PictureOfTheDayResponseData>)
    fun getEarthPhotosDates(callBack: RetrofitCallBack<DatesEarthPhotosResponse>)
    fun getEarthPhotosData(date: String, callBack: RetrofitCallBack<EarthPhotoDataResponse>)
    fun getMarsRoverManifest(callBack: RetrofitCallBack<MarsRoverManifestResponse>)
}