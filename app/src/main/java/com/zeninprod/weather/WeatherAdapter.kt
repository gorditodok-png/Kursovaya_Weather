package com.zeninprod.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zeninprod.weather.databinding.ItemCityWeatherBinding

class WeatherAdapter(private val onCityClick: (CityWeather) -> Unit) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private var cities = listOf<CityWeather>()

    fun submitList(newList: List<CityWeather>) {
        cities = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemCityWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding, onCityClick)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(cities[position])
    }

    override fun getItemCount(): Int = cities.size

    class WeatherViewHolder(
        private val binding: ItemCityWeatherBinding,
        private val onCityClick: (CityWeather) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CityWeather) {
            val context = itemView.context
            binding.textViewCityName.text = context.getString(item.cityNameResId)
            
            itemView.setOnClickListener {
                if (!item.isLoading && item.errorResId == null) {
                    onCityClick(item)
                }
            }

            if (item.isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.detailsLayout.visibility = View.GONE
                binding.textViewTemperature.text = ""
                binding.textViewFeelsLike.text = ""
                binding.textViewWeatherDescription.text = ""
            } else {
                binding.progressBar.visibility = View.GONE
                if (item.errorResId != null) {
                    binding.textViewTemperature.text = context.getString(R.string.error)
                    binding.detailsLayout.visibility = View.GONE
                    binding.textViewFeelsLike.text = ""
                    binding.textViewWeatherDescription.text = context.getString(item.errorResId)
                } else {
                    binding.detailsLayout.visibility = View.VISIBLE
                    binding.textViewWeatherDescription.text = item.weatherDescriptionResId?.let { context.getString(it) } ?: ""
                    binding.textViewTemperature.text = context.getString(R.string.temp_unit, item.temperature)
                    binding.textViewFeelsLike.text = context.getString(R.string.feels_like, item.feelsLike)
                    binding.textViewHumidity.text = context.getString(R.string.humidity_format, item.humidity)
                    binding.textViewChanceOfRain.text = context.getString(R.string.chance_of_rain_format, item.chanceOfRain)
                    binding.textViewWind.text = context.getString(R.string.wind_speed_format, item.windSpeed)
                }
            }
        }
    }
}
