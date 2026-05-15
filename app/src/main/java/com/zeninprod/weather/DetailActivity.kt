package com.zeninprod.weather

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.zeninprod.weather.data.WeatherRepository
import com.zeninprod.weather.data.WeatherUtils
import com.zeninprod.weather.databinding.ActivityDetailBinding
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val repository = WeatherRepository()
    private val adapter = DailyWeatherAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.detailMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }

        val cityNameResId = intent.getIntExtra("CITY_NAME_RES_ID", 0)
        val lat = intent.getDoubleExtra("LAT", 0.0)
        val lon = intent.getDoubleExtra("LON", 0.0)

        if (cityNameResId != 0) {
            binding.textViewCityNameDetail.text = getString(cityNameResId)
        }
        binding.recyclerViewDaily.adapter = adapter

        fetchWeeklyWeather(lat, lon)
    }

    private fun fetchWeeklyWeather(lat: Double, lon: Double) {
        binding.textViewStatus.text = getString(R.string.loading_forecast)
        binding.textViewStatus.visibility = View.VISIBLE
        binding.recyclerViewDaily.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = repository.getWeather(lat, lon)
                val daily = response?.daily
                if (daily != null) {
                    val russianLocale = Locale.forLanguageTag("ru")
                    val dayFormat = DateTimeFormatter.ofPattern("EEEE", russianLocale)
                    val dateFormat = DateTimeFormatter.ofPattern("d MMMM", russianLocale)

                    val today = response.currentWeather?.time?.take(10)?.let(LocalDate::parse)
                        ?: daily.time.firstOrNull()?.let(LocalDate::parse)
                    val tomorrow = today?.plusDays(1)

                    val weatherList: List<DailyWeather> = daily.time.mapIndexed { index, timeStr ->
                        val date = LocalDate.parse(timeStr)

                        val dayName = when {
                            date == today -> getString(R.string.today)
                            date == tomorrow -> getString(R.string.tomorrow)
                            else -> dayFormat.format(date).replaceFirstChar {
                                it.uppercase(russianLocale)
                            }
                        }

                        DailyWeather(
                            date = dateFormat.format(date),
                            dayName = dayName,
                            tempMax = daily.tempMax[index].roundToInt(),
                            tempMin = daily.tempMin[index].roundToInt(),
                            weatherDescriptionResId = WeatherUtils.getWeatherDescriptionResId(
                                daily.weatherCodes?.getOrNull(index)
                            )
                        )
                    }
                    adapter.submitList(weatherList)
                    binding.textViewStatus.visibility = View.GONE
                    binding.recyclerViewDaily.visibility = View.VISIBLE
                } else {
                    binding.textViewStatus.text = getString(R.string.unable_to_load_forecast)
                }
            } catch (_: Exception) {
                binding.textViewStatus.text = getString(R.string.unable_to_load_forecast)
                binding.textViewStatus.visibility = View.VISIBLE
                binding.recyclerViewDaily.visibility = View.GONE
            }
        }
    }
}
