package com.jithin.weatherapp

// WeatherRepository.kt

import retrofit2.Retrofit
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val retrofit: Retrofit) {

    suspend fun getWeatherData(city: String): WeatherData? {
        return try {
            val response = retrofit.create(WeatherService::class.java).getWeather(city, "9b8cb8c7f11c077f8c4e217974d9ee40")
            if (response.isSuccessful) {
                val weatherResponse = response.body()
                weatherResponse?.let {
                    WeatherData(
                        weatherResponse.temperature,
                        weatherResponse.humidity,
                        weatherResponse.description
                    )
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
