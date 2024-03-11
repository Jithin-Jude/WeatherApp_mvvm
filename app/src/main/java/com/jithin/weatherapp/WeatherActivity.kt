package com.jithin.weatherapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        observeViewModel()
        viewModel.fetchWeather(city = "Bengaluru")
    }

    private fun observeViewModel() {
        viewModel.weatherData.observe(this) { weatherData ->
            Toast.makeText(this, "CURRENT_TEMP :=> ${weatherData.temperature}", Toast.LENGTH_LONG)
                .show()
        }
    }
}