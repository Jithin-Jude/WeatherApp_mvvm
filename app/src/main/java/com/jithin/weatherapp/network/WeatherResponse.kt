package com.jithin.weatherapp.network

import com.google.gson.annotations.SerializedName


data class CurrentWeatherResponse(
    @SerializedName("main") val currentTemperature: CurrentTemperature,
    @SerializedName("name") val name: String,
)

data class WeatherForecastResponse(
    @SerializedName("list") val weatherList: List<WeatherData>,
)

data class WeatherData(
    @SerializedName("dt") val dt: Long,
    @SerializedName("main") val temperature: CurrentTemperature,
)

data class CurrentTemperature(
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int
)
