package com.jithin.weatherapp.network

// WeatherRepository.kt

import com.jithin.weatherapp.database.Temperature
import com.jithin.weatherapp.database.TemperatureDao
import com.jithin.weatherapp.model.CurrentWeatherData
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val retrofit: Retrofit,
    private val temperatureDao: TemperatureDao
) {

    val apiService = retrofit.create(WeatherService::class.java)

    suspend fun insertTemperatures(temperature: Temperature) {
        temperatureDao.insertTemperature(temperature)
    }

    suspend fun fetchAllTemperatures() = flow<DataState<List<Temperature>>> {
        emit(DataState.Loading)
        val list = temperatureDao.getAllTemperatures()
        emit(DataState.Success(list))
    }

    suspend fun getCurrentWeatherData(city: String) = flow<DataState<CurrentWeatherData>> {
        try {
            emit(DataState.Loading)
            val response = apiService.getCurrentWeather(city)
            if (response.isSuccessful) {
                val weatherResponse = response.body()
                weatherResponse?.let {
                    // just mapping only no business logic
                    val data = CurrentWeatherData(
                        weatherResponse.currentTemperature.temp,
                        weatherResponse.name
                    )
                    emit(DataState.Success(data))
                } ?: kotlin.run {
                    emit(DataState.Error(Exception(response.message())))
                }
            } else {
                emit(DataState.Error(Exception(response.message())))
            }
        } catch (e: Exception) {
            emit(DataState.Error(Exception("error")))
        }
    }

    suspend fun getForecastData(city: String) = flow<DataState<List<WeatherData>>> {
        try {
            emit(DataState.Loading)
            val response = apiService.getForecast(city)
            if (response.isSuccessful) {
                val weatherForecastResponse = response.body()
                weatherForecastResponse?.let {
                    emit(DataState.Success(weatherForecastResponse.weatherList))
                } ?: kotlin.run {
                    emit(DataState.Error(Exception(response.message())))
                }
            } else {
                emit(DataState.Error(Exception(response.message())))
            }
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}
