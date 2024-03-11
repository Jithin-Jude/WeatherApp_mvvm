package com.jithin.weatherapp

data class WeatherForecastData(
    val days: List<WeatherForecastDay>,
)

data class WeatherForecastDay(
    val day: String,
    val avgTemperature: Double,
)