package com.jithin.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.jithin.weatherapp.database.AppDatabase
import com.jithin.weatherapp.database.TemperatureDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideTemperatureDao(appDatabase: AppDatabase): TemperatureDao {
        return appDatabase.temperatureDao()
    }

    // Other DAOs and dependencies can be provided similarly
}
