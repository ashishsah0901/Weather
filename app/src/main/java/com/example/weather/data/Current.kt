package com.example.weather.data

import java.io.Serializable

data class Current(
    val clouds: Float,
    val dew_point: Float,
    val dt: Long,
    val feels_like: Float,
    val humidity: Float,
    val pressure: Float,
    val sunrise: Float,
    val sunset: Float,
    val temp: Float,
    val uvi: Float,
    val visibility: Float,
    val weather: List<Weather>,
    val wind_deg: Float,
    val wind_speed: Float
): Serializable