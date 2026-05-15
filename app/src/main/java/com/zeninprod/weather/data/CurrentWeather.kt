package com.zeninprod.weather.data

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    val temperature: Double,
    val windspeed: Double,
    @SerializedName("weathercode")
    val weatherCode: Int,
    val time: String
)
