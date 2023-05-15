package com.teameverywhere.taximobility.repository

import com.teameverywhere.taximobility.network.WeatherApi
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val apiService: WeatherApi) {

    suspend fun getWeather() = apiService.getWeather("daegu")
}