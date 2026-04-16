package com.zeninprod.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeninprod.weather.data.WeatherRepository
import com.zeninprod.weather.data.WeatherUtils
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()

    private val _citiesWeather = MutableLiveData<List<CityWeather>>()
    val citiesWeather: LiveData<List<CityWeather>> = _citiesWeather

    private val cities = mapOf(
        R.string.city_izhevsk to "Izhevsk",
        R.string.city_moscow to "Moscow",
        R.string.city_spb to "Saint Petersburg",
        R.string.city_kazan to "Kazan",
        R.string.city_sochi to "Sochi"
    )

    fun fetchWeather() {
        val initialList = cities.keys.map { CityWeather(it, isLoading = true) }
        _citiesWeather.value = initialList

        viewModelScope.launch {
            val updatedList = cities.map { (russianNameRes, englishName) ->
                async {
                    try {
                        val coords = repository.getCityCoordinates(englishName)
                        if (coords != null) {
                            val weather = repository.getWeather(coords.latitude, coords.longitude)
                            val hourly = weather?.hourly
                            val current = weather?.currentWeather

                            val currentHourString = current?.time
                                ?.takeIf { it.length >= 13 }
                                ?.let { "${it.substring(0, 13)}:00" }

                            val currentTimeIndex = currentHourString
                                ?.let { hour -> hourly?.time?.indexOf(hour) }
                                ?: -1

                            val rawTemp = current?.temperature ?: hourly?.temperature2m?.firstOrNull()

                            val rawFeelsLike = if (currentTimeIndex != -1) {
                                hourly?.apparentTemperature?.getOrNull(currentTimeIndex)
                            } else {
                                hourly?.apparentTemperature?.firstOrNull()
                            }

                            val rawHumidity = if (currentTimeIndex != -1) {
                                hourly?.humidity2m?.getOrNull(currentTimeIndex)
                            } else {
                                hourly?.humidity2m?.firstOrNull()
                            }

                            val rawChanceOfRain = if (currentTimeIndex != -1) {
                                hourly?.precipitationProbability?.getOrNull(currentTimeIndex)
                            } else {
                                hourly?.precipitationProbability?.firstOrNull()
                            }

                            CityWeather(
                                cityNameResId = russianNameRes,
                                lat = coords.latitude,
                                lon = coords.longitude,
                                temperature = rawTemp?.roundToInt(),
                                feelsLike = rawFeelsLike?.roundToInt(),
                                humidity = rawHumidity,
                                chanceOfRain = rawChanceOfRain,
                                windSpeed = current?.windspeed ?: hourly?.windSpeed10m?.firstOrNull(),
                                weatherDescriptionResId = WeatherUtils.getWeatherDescriptionResId(current?.weatherCode)
                            )
                        } else {
                            CityWeather(russianNameRes, errorResId = R.string.city_not_found)
                        }
                    } catch (e: Exception) {
                        CityWeather(
                            russianNameRes,
                            errorResId = R.string.unable_to_load_forecast
                        )
                    }
                }
            }.awaitAll()

            _citiesWeather.postValue(updatedList)
        }
    }
}
