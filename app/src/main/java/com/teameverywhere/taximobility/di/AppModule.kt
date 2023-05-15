package com.teameverywhere.taximobility.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.teameverywhere.taximobility.data.CarDao
import com.teameverywhere.taximobility.data.CarDatabase
import com.teameverywhere.taximobility.network.NavigationApi
import com.teameverywhere.taximobility.network.SearchApi
import com.teameverywhere.taximobility.network.WeatherApi
import com.teameverywhere.taximobility.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideWeatherBaseUrl() = Constant.WEATHER_BASE_URL

    @Provides
    fun provideSearchBaseUrl() = Constant.TMAP_BASE_URL

    @Singleton
    @Provides
    fun provideRetrofitInstance(): WeatherApi{
        return Retrofit.Builder()
            .baseUrl(Constant.WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

    @Singleton
    @Provides
    fun provideSearchInstance(): SearchApi{
        return Retrofit.Builder()
            .baseUrl(Constant.TMAP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDirectionInstance(): NavigationApi{
        return Retrofit.Builder()
            .baseUrl(Constant.TMAP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NavigationApi::class.java)
    }

    @Singleton
    @Provides
    fun provideCarDatabase(@ApplicationContext context: Context): CarDatabase {
        return Room.databaseBuilder(
            context,
            CarDatabase::class.java,
            "car_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideCarDao(carDatabase: CarDatabase): CarDao {
        return carDatabase.carDao()
    }
}