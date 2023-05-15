package com.teameverywhere.taximobility.network

import com.teameverywhere.taximobility.model.Weather
import com.teameverywhere.taximobility.util.Constant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

interface WeatherApi {
    @GET("data/2.5/forecast/daily")
    suspend fun getWeather(
        @Query("q") query: String,
        @Query("units") units: String = "metric",
        @Query("appid") appid: String = Constant.API_KEY
    ): Response<Weather>
}