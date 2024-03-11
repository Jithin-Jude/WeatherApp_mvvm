package com.jithin.weatherapp

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
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
            updateUI(weatherData?.temperature, weatherData?.cityName)
        }
        viewModel.weatherForecastData.observe(this) { weatherForecastData ->
            handleLoader(false)
            if (weatherForecastData != null) {
                val bottomSheet =
                    ForecastBottomSheetDialogFragment.newInstance(
                        weatherForecastData?.days ?: emptyList()
                    )
                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            } else {
                showSnackBar()
            }
        }
    }

    private fun updateUI(temperature: Double?, cityName: String?) {
        val tempInCelsius = kelvinToCelsius(temperature ?: 0.0)
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

    private fun showSnackBar() {
        val snackbar =
            Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("RETRY") {
            viewModel.fetchWeather(city = "Bengaluru")
            handleLoader(true)
        }
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.deep_orange))
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.black))
        val textView =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))

        snackbar.show()
    }
}