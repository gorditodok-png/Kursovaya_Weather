package com.zeninprod.weather.data

import com.google.gson.annotations.SerializedName

data class HourlyData(
    val time: List<String>,
    @SerializedName("temperature_2m")
    val temperature2m: List<Double>,
    @SerializedName("relative_humidity_2m")
    val humidity2m: List<Double>?,
    @SerializedName("apparent_temperature")
    val apparentTemperature: List<Double>?,
    @SerializedName("precipitation_probability")
    val precipitationProbability: List<Int>?,
    @SerializedName("wind_speed_10m")
    val windSpeed10m: List<Double>?
)
