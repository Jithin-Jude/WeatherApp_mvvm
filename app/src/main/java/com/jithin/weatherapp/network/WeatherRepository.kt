package com.jithin.weatherapp.network

// WeatherRepository.kt

import android.util.Log
import com.jithin.weatherapp.getDayOfWeek
import com.jithin.weatherapp.kelvinToCelsius
import com.jithin.weatherapp.model.CurrentWeatherData
import com.jithin.weatherapp.model.WeatherForecastData
import com.jithin.weatherapp.model.WeatherForecastDay
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val retrofit: Retrofit) {

    val apiService = retrofit.create(WeatherService::class.java)

    suspend fun getCurrentWeatherData(city: String) = flow<DataState<CurrentWeatherData>> {
        try {
            emit(DataState.Loading)
            val response = apiService.getCurrentWeather(city)
            if (response.isSuccessful) {
                val weatherResponse = response.body()
                weatherResponse?.let {
                    val data = CurrentWeatherData(
                        weatherResponse.currentTemperature.temp,
                        weatherResponse.name
                    )
                    emit(DataState.Success(data))
                } ?: kotlin.run {
                    emit(DataState.Error(Exception(response.message())))
                }
            } else {
                emit(DataState.Error(Exception(response.message())))
            }
        } catch (e: Exception) {
            emit(DataState.Error(Exception("error")))
        }
    }

    suspend fun getForecastData(city: String) = flow<DataState<WeatherForecastData>> {
        try {
            emit(DataState.Loading)
            val response = apiService.getForecast(city)
            if (response.isSuccessful) {
                val weatherForecastResponse = response.body()
                weatherForecastResponse?.let {
                    val groupedByDay = weatherForecastResponse.weatherList.groupBy {
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
                    val data = WeatherForecastData(days = nextFourDays)
                    emit(DataState.Success(data))
                } ?: kotlin.run {
                    emit(DataState.Error(Exception(response.message())))
                }
            } else {
                emit(DataState.Error(Exception(response.message())))
            }
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}
