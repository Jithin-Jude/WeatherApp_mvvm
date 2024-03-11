package com.jithin.weatherapp

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("temp")
    val temperature: Double,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("description")
    val description: String
)
