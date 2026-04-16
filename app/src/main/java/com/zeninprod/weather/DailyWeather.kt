package com.zeninprod.weather

data class DailyWeather(
    val date: String,
    val dayName: String,
    val tempMax: Int,
    val tempMin: Int,
    val weatherDescriptionResId: Int
)
