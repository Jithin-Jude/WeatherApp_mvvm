package com.jithin.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {

    private val _currentWeatherData = MutableLiveData<CurrentWeatherData>()
    val currentWeatherData: LiveData<CurrentWeatherData>
        get() = _currentWeatherData

    private val _weatherForecastData = MutableLiveData<WeatherForecastData>()
    val weatherForecastData: LiveData<WeatherForecastData>
        get() = _weatherForecastData

    fun fetchWeather(city: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val result = repository.getCurrentWeatherData(city)
            val resultWeatherForecast = repository.getForecastData(city)
            result?.let {
                _currentWeatherData.postValue(it)
            }
            resultWeatherForecast?.let {
                _weatherForecastData.postValue(it)
            }
        }
    }
}