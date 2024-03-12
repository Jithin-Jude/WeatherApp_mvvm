package com.jithin.weatherapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class WeatherForecastData(
    val days: List<WeatherForecastDay>,
)

@Parcelize
data class WeatherForecastDay(
    val day: String,
    val avgTemperature: Int,
) : Parcelable