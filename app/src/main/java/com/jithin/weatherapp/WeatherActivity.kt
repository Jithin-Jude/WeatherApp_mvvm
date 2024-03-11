package com.jithin.weatherapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jithin.weatherapp.databinding.ActivityWeatherBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()
        viewModel.fetchWeather(city = "Bengaluru")
    }

    private fun observeViewModel() {
        viewModel.currentWeatherData.observe(this) { weatherData ->
            Toast.makeText(this, "CURRENT_TEMP :=> ${weatherData.temperature}", Toast.LENGTH_LONG)
                .show()
            updateUI(weatherData.temperature, weatherData.cityName)
        }
    }

    private fun updateUI(temperature: Double, cityName: String) {
        val tempInCelsius = kelvinToCelsius(temperature)
        binding.tvCityTemperature.text = "$tempInCelsius\u00B0"
        binding.tvCityName.text = cityName
    }
}