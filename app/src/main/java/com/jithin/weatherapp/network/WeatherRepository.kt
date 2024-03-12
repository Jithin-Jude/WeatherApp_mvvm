package com.jithin.weatherapp.network

// WeatherRepository.kt

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
                    val data = WeatherForecastData(days = listOfDays.drop(1))
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
