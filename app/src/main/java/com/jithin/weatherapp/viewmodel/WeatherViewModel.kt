package com.jithin.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jithin.weatherapp.model.CurrentWeatherData
import com.jithin.weatherapp.model.WeatherForecastData
import com.jithin.weatherapp.network.DataState
import com.jithin.weatherapp.network.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {

    private val _currentWeatherData = MutableLiveData<CurrentWeatherData?>()
    val currentWeatherData: LiveData<CurrentWeatherData?>
        get() = _currentWeatherData

    private val _weatherForecastData = MutableLiveData<WeatherForecastData?>()
    val weatherForecastData: LiveData<WeatherForecastData?>
        get() = _weatherForecastData

    private val _loader = MutableLiveData<Boolean>()
    val loader: LiveData<Boolean>
        get() = _loader

    fun fetchWeather(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCurrentWeatherData(city).collect { result ->
                when (result) {
                    is DataState.Loading -> {
                        _loader.postValue(true)
                    }

                    is DataState.Success -> {
                        _currentWeatherData.postValue(result.data)
                    }

                    is DataState.Error -> {
                        _currentWeatherData.postValue(null)
                    }
                }
            }
            repository.getForecastData(city).collect { result ->
                when (result) {
                    is DataState.Loading -> {
                        _loader.postValue(true)
                    }

                    is DataState.Success -> {
                        _weatherForecastData.postValue(result.data)
                        _loader.postValue(false)
                    }

                    is DataState.Error -> {
                        _weatherForecastData.postValue(null)
                        _loader.postValue(false)
                    }
                }
            }
        }
    }
}