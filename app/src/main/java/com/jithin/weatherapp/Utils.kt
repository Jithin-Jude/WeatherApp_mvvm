package com.jithin.weatherapp

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun kelvinToCelsius(kelvin: Double): Int {
    return (kelvin - 273.15).toInt()
}

fun getDayOfWeek(epochTimestamp: Long): String {
    // Create a calendar instance and set its time to the epoch timestamp
    val calendar = Calendar.getInstance().apply {
        timeInMillis = epochTimestamp * 1000 // Convert seconds to milliseconds
    }

    // Define the date format to get the day of the week (full name)
    val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())

    // Format the date and return the day of the week
    return dateFormat.format(calendar.time)
}