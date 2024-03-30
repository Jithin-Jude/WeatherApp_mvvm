package com.jithin.weatherapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "temperatures")
data class Temperature(
    @PrimaryKey val id: String,
    val temperature: String,
)
