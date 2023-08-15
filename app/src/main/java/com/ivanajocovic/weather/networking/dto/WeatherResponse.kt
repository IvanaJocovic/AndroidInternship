package com.ivanajocovic.weather.networking.dto

data class WeatherResponse(
    val weather: List<WeatherDescriptionResponse?> = emptyList(),
    val main: MainResponse? = null,
    val name: String? = null
)
