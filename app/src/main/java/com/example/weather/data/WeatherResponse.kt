package com.example.weather.data

import java.io.Serializable

data class WeatherResponse(
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Float,
    val lon: Float,
    val timezone: String,
    val timezone_offset: Float
): Serializable