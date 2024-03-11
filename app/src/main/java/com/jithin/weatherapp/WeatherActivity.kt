package com.jithin.weatherapp

import android.os.Bundle
import android.view.View
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
        handleLoader(true)
    }

    private fun observeViewModel() {
        viewModel.currentWeatherData.observe(this) { weatherData ->
            updateUI(weatherData.temperature, weatherData.cityName)
        }
        viewModel.weatherForecastData.observe(this) { weatherForecastData ->
            val bottomSheet =
                ForecastBottomSheetDialogFragment.newInstance(weatherForecastData.days)
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            handleLoader(false)
        }
    }

    private fun updateUI(temperature: Double, cityName: String) {
        val tempInCelsius = kelvinToCelsius(temperature)
        binding.tvCityTemperature.text = "$tempInCelsius\u00B0"
        binding.tvCityName.text = cityName
    }

    private fun handleLoader(show: Boolean) {
        if (show) {
            binding.loadingView.visibility = View.VISIBLE
        } else {
            binding.loadingView.visibility = View.GONE
        }
    }
}