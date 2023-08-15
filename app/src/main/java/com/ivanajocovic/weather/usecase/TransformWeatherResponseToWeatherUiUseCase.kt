package com.ivanajocovic.weather.usecase

import com.ivanajocovic.weather.networking.dto.WeatherResponse
import com.ivanajocovic.weather.ui.CityWeatherUi
import javax.inject.Inject

class TransformWeatherResponseToWeatherUiUseCase @Inject constructor() {

    operator fun invoke(response: WeatherResponse): CityWeatherUi {
        return CityWeatherUi(
            cityName = response.name ?: "",
            temperature = response.main?.temp ?: 0.0,
            description = response.weather.firstOrNull()?.description ?: "",
            icon = response.weather.firstOrNull()?.icon ?: ""
        )
    }
}