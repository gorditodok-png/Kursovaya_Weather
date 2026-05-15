package com.zeninprod.weather.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepository {
    private val apiNinjasService = Retrofit.Builder()
        .baseUrl("https://api.api-ninjas.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiNinjasService::class.java)

    private val openMeteoService = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OpenMeteoService::class.java)

    private val apiKey = "p4HPMkYEoddXLx4uSSkgmMV5wUxU30j61SOJyDWV"

    suspend fun getCityCoordinates(cityName: String): CityResponse? {
        return try {
            val response = apiNinjasService.getCityDetails(cityName, apiKey)
            response.firstOrNull()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getWeather(lat: Double, lon: Double): WeatherResponse? {
        return try {
            openMeteoService.getForecast(lat, lon)
        } catch (e: Exception) {
            null
        }
    }
}
