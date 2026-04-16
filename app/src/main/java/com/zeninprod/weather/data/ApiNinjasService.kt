package com.zeninprod.weather.data

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiNinjasService {
    @GET("v1/city")
    suspend fun getCityDetails(
        @Query("name") cityName: String,
        @Header("X-Api-Key") apiKey: String
    ): List<CityResponse>
}
