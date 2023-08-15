package com.ivanajocovic.weather.ui

import java.time.LocalDate
import java.time.LocalDateTime

data class CityWeatherUi(
    val cityName: String,
    val temperature: Double,
    val description: String,
    val icon: String
)

