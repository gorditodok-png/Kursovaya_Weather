package com.zeninprod.weather

data class CityWeather(
    val cityNameResId: Int,
    val cityName: String? = null,
    val lat: Double? = null,
    val lon: Double? = null,
    val temperature: Int? = null,
    val feelsLike: Int? = null,
    val humidity: Double? = null,
    val chanceOfRain: Int? = null,
    val windSpeed: Double? = null,
    val weatherDescriptionResId: Int? = null,
    val isLoading: Boolean = false,
    val errorResId: Int? = null
)
