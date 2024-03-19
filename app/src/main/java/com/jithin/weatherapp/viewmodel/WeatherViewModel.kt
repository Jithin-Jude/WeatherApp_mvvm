package com.jithin.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jithin.weatherapp.getDayOfWeek
import com.jithin.weatherapp.kelvinToCelsius
import com.jithin.weatherapp.model.CurrentWeatherData
import com.jithin.weatherapp.model.WeatherForecastData
import com.jithin.weatherapp.model.WeatherForecastDay
import com.jithin.weatherapp.network.DataState
import com.jithin.weatherapp.network.WeatherData
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
                        val data = filterForecastDays(result.data)
                        _weatherForecastData.postValue(data)
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

    private fun filterForecastDays(data: List<WeatherData>): WeatherForecastData {
        val groupedByDay = data.groupBy {
            getDayOfWeek(it.dt)
        }
        val listOfDays = groupedByDay.map { (day, forecasts) ->
            val avgTemperature = forecasts.map { it.temperature.temp }.average()
            WeatherForecastDay(day, kelvinToCelsius(avgTemperature))
        }
        // Current day
        val today = getDayOfWeek(System.currentTimeMillis() / 1000)
        Log.d("TAG", "TODAY :=> ${today}")
        // Find the index of today in the list
        val nextDayIndex = listOfDays.indexOfFirst { it.day == today } + 1
        // Get the sublist containing the next 4 days
        val nextFourDays = if (nextDayIndex > 0) {
            // If today is found in the list, get the next 4 days
            listOfDays.subList(nextDayIndex, minOf(nextDayIndex + 4, listOfDays.size))
        } else {
            // If today is not found in the list, start from the first day
            listOfDays.subList(0, minOf(4, listOfDays.size))
        }
        return WeatherForecastData(days = nextFourDays)
    }
}