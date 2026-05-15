package com.zeninprod.weather.data

import com.google.gson.annotations.SerializedName

data class CityResponse(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val population: Int,
    @SerializedName("is_capital")
    val isCapital: Boolean
)
