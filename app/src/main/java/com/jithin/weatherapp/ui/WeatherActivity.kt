package com.jithin.weatherapp.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.jithin.weatherapp.R
import com.jithin.weatherapp.databinding.ActivityWeatherBinding
import com.jithin.weatherapp.kelvinToCelsius
import com.jithin.weatherapp.model.WeatherForecastDay
import com.jithin.weatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeWeatherData()
        observeLoader()
        viewModel.fetchWeather(city = "Bengaluru")
        viewModel.fetchAllTemperatures()
    }

    private fun observeWeatherData() {
        viewModel.currentWeatherData.observe(this) { weatherData ->
            weatherData?.let {
                viewModel.insertTemperature(it)
                updateCurrentWeatherUI(it.temperature, it.cityName)
            }
        }
        viewModel.weatherForecastData.observe(this) { weatherForecastData ->
            if (weatherForecastData != null) {
                openForecastBottomSheet(weatherForecastData.days)
            } else {
                showSnackBar()
            }
        }
    }

    private fun observeLoader() {
        viewModel.loader.observe(this) { show ->
            if (show) {
                binding.loadingView.visibility = View.VISIBLE
            } else {
                binding.loadingView.visibility = View.GONE
            }
        }
    }

    private fun updateCurrentWeatherUI(temperature: Double?, cityName: String?) {
        val tempInCelsius = kelvinToCelsius(temperature ?: 0.0)
        binding.tvCityTemperature.text = "$tempInCelsius\u00B0"
        binding.tvCityName.text = cityName
    }

    private fun openForecastBottomSheet(weatherForecastDays: List<WeatherForecastDay>) {
        val bottomSheet =
            ForecastBottomSheetDialogFragment.newInstance(
                weatherForecastDays
            )
        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
    }

    private fun showSnackBar() {
        val snackbar =
            Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("RETRY") {
            viewModel.fetchWeather(city = "Bengaluru")
        }
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.deep_orange))
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.black))
        val textView =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))

        snackbar.show()
    }
}