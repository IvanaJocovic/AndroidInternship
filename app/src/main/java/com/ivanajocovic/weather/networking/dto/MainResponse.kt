package com.ivanajocovic.weather.networking.dto

import com.google.gson.annotations.SerializedName

data class MainResponse(
    val temp: Double? = null,
    @SerializedName("temp_min") val tempMin: Double? = null,
    @SerializedName("temp_max") val tempMax: Double? = null,
)
