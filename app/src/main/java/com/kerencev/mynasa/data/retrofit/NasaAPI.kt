package com.kerencev.mynasa.data.retrofit

import com.kerencev.mynasa.data.retrofit.entities.dates.DatesEarthPhotosResponse
import com.kerencev.mynasa.data.retrofit.entities.mars.rovermanifest.MarsRoverManifestResponse
import com.kerencev.mynasa.data.retrofit.entities.mars.roverphotos.RoverPhotosResponse
import com.kerencev.mynasa.data.retrofit.entities.photo.EarthPhotoDataResponse
import com.kerencev.mynasa.data.retrofit.entities.pictureoftheday.PictureOfTheDayResponseData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NasaAPI {

    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey: String): Call<PictureOfTheDayResponseData>

    @GET("planetary/apod")
    fun getPictureOfTheDayByDate(
        @Query("api_key") apiKey: String,
        @Query("date") date: String
    ): Call<PictureOfTheDayResponseData>

    @GET("EPIC/api/natural/all")
    fun getEarthPhotosDates(@Query("api_key") apiKey: String): Call<DatesEarthPhotosResponse>

    @GET("EPIC/api/natural/date/{date}")
    fun getEarthPhotoData(
        @Path("date") date: String,
        @Query("api_key") apiKey: String
    ): Call<EarthPhotoDataResponse>

    @GET("mars-photos/api/v1/manifests/curiosity")
    fun getRoverManifest(@Query("api_key") apiKey: String): Call<MarsRoverManifestResponse>

    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun getRoverPhotos(
        @Query("earth_date") date: String,
        @Query("api_key") apiKey: String
    ): Call<RoverPhotosResponse>

    companion object {

        private const val BASE_URL = "https://api.nasa.gov/"
        fun create(): NasaAPI {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(NasaAPI::class.java)
        }
    }
}