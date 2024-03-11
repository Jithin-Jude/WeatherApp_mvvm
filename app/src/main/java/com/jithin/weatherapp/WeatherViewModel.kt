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

    private val _Current_weatherData = MutableLiveData<CurrentWeatherData>()
    val currentWeatherData: LiveData<CurrentWeatherData>
        get() = _Current_weatherData

    fun fetchWeather(city: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val result = repository.getWeatherData(city)
            result?.let {
                _Current_weatherData.postValue(it)
            }
        }
    }
}