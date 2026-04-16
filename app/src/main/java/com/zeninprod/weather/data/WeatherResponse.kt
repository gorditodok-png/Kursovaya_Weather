package com.zeninprod.weather.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    @SerializedName("current_weather")
    val currentWeather: CurrentWeather?,
    @SerializedName("hourly")
    val hourly: HourlyData?,
    @SerializedName("daily")
    val daily: DailyData?
)
