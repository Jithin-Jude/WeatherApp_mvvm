package com.jithin.weatherapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("APPID") apiKey: String = "9b8cb8c7f11c077f8c4e217974d9ee40"
    ): Response<WeatherResponse>
}