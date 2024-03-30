package com.jithin.weatherapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TemperatureDao {
    @Query("SELECT * FROM temperatures")
    suspend fun getAllTemperatures(): List<Temperature>

    @Insert
    suspend fun insertTemperature(user: Temperature)

    @Query("DELETE FROM temperatures")
    suspend fun deleteAllTemperatures()
}