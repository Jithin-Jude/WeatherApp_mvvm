package com.jithin.weatherapp.network

import com.jithin.weatherapp.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("APPID") apiKey: String = Constants.OPEN_WEATHER_MAP_API_KEY
    ): Response<CurrentWeatherResponse>

    @GET("forecast")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("APPID") apiKey: String = Constants.OPEN_WEATHER_MAP_API_KEY
    ): Response<WeatherForecastResponse>
}