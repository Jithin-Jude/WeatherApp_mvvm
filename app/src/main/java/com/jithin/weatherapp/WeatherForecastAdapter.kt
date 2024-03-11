package com.jithin.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jithin.weatherapp.databinding.LayoutForecastItemBinding

class WeatherForecastAdapter(private val weatherForecastDays: List<WeatherForecastDay>) :
    RecyclerView.Adapter<WeatherForecastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutForecastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weatherForecastDay = weatherForecastDays[position]
        holder.bind(weatherForecastDay, position)
    }

    override fun getItemCount(): Int {
        return weatherForecastDays.size
    }

    inner class ViewHolder(private val binding: LayoutForecastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(weatherForecastDay: WeatherForecastDay, position: Int) {
            binding.tvDay.text = weatherForecastDay.day
            binding.tvTemperature.text = "${weatherForecastDay.avgTemperature}\u00B0"

            val isLastItem = position == itemCount - 1
            if (isLastItem) {
                binding.divider.visibility = View.GONE
            }
        }
    }
}