package com.zeninprod.weather

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeninprod.weather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()
    private val adapter = WeatherAdapter { cityWeather ->
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("CITY_NAME_RES_ID", cityWeather.cityNameResId)
            putExtra("LAT", cityWeather.lat)
            putExtra("LON", cityWeather.lon)
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        observeViewModel()

        if (savedInstanceState == null) {
            viewModel.fetchWeather()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewWeather.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewWeather.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.citiesWeather.observe(this) { weatherList ->
            adapter.submitList(weatherList)
        }
    }
}
