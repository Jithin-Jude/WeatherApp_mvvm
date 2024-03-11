package com.jithin.weatherapp

// WeatherRepository.kt

import retrofit2.Retrofit
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val retrofit: Retrofit) {

    val apiService = retrofit.create(WeatherService::class.java)

    suspend fun getCurrentWeatherData(city: String): CurrentWeatherData? {
        return try {
            val response = apiService.getCurrentWeather(city)
            if (response.isSuccessful) {
                val weatherResponse = response.body()
                weatherResponse?.let {
                    CurrentWeatherData(
                        weatherResponse.currentTemperature.temp,
                        weatherResponse.name
                    )
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getForecastData(city: String): WeatherForecastData? {
        return try {
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
                    WeatherForecastData(days = listOfDays.drop(1))
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
