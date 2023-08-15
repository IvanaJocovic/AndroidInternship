package com.ivanajocovic.weather.networking.datasource

import com.ivanajocovic.weather.networking.api.WeatherApiService
import com.ivanajocovic.weather.networking.dto.WeatherResponse
import javax.inject.Inject

class WeatherDataSource @Inject constructor(
    private val apiService: WeatherApiService
) {

    suspend fun getForecast(
        cityName: String
    ): WeatherResponse {

        return apiService.getForecast(
            cityName = cityName
        )
    }
}
