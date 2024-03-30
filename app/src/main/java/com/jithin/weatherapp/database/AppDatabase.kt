package com.jithin.weatherapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Temperature::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun temperatureDao(): TemperatureDao
}