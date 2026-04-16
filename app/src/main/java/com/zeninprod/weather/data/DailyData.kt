package com.zeninprod.weather.data

import com.google.gson.annotations.SerializedName

data class DailyData(
    val time: List<String>,
    @SerializedName("temperature_2m_max")
    val tempMax: List<Double>,
    @SerializedName("temperature_2m_min")
    val tempMin: List<Double>,
    @SerializedName("precipitation_sum")
    val precipitationSum: List<Double>?,
    @SerializedName("weathercode")
    val weatherCodes: List<Int>?
)
