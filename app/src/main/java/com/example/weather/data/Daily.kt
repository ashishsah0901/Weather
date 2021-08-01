package com.example.weather.data

import java.io.Serializable

data class Daily(
    val clouds: Float,
    val dew_point: Float,
    val dt: Float,
    val feels_like: FeelsLike,
    val humidity: Float,
    val moon_phase: Float,
    val moonrise: Float,
    val moonset: Float,
    val pop: Float,
    val pressure: Float,
    val rain: Float,
    val sunrise: Float,
    val sunset: Float,
    val temp: Temp,
    val uvi: Float,
    val weather: List<Weather>,
    val wind_deg: Float,
    val wind_gust: Float,
    val wind_speed: Float
): Serializable