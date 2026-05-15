package com.zeninprod.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zeninprod.weather.databinding.ItemDailyWeatherBinding

class DailyWeatherAdapter : RecyclerView.Adapter<DailyWeatherAdapter.DailyViewHolder>() {

    private var items = listOf<DailyWeather>()

    fun submitList(newList: List<DailyWeather>) {
        items = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val binding = ItemDailyWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class DailyViewHolder(private val binding: ItemDailyWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DailyWeather) {
            val context = itemView.context
            binding.textViewDailyDayName.text = item.dayName
            binding.textViewDate.text = context.getString(
                R.string.date_with_desc,
                item.date,
                context.getString(item.weatherDescriptionResId)
            )
            binding.textViewDailyTemp.text = context.getString(R.string.daily_temp_format, item.tempMax, item.tempMin)
        }
    }
}
