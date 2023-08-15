package com.ivanajocovic.weather.networking.api

import com.ivanajocovic.weather.networking.dto.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("/data/2.5/weather")
    suspend fun getForecast(
        @Query("q") cityName: String,
        @Query("units") units: String = "metric",
        @Query("APPID") appid: String = "e986238f5a6bb6df38b02d57c8df8efe"
    ): WeatherResponse
}