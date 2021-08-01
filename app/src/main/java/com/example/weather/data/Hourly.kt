package com.example.weather.data

import java.io.Serializable

data class Hourly(
    val clouds: Float,
    val dew_point: Float,
    val dt: Float,
    val feels_like: Float,
    val humidity: Float,
    val pop: Float,
    val pressure: Float,
    val rain: Rain,
    val temp: Float,
    val uvi: Float,
    val visibility: Float,
    val weather: List<Weather>,
    val wind_deg: Float,
    val wind_gust: Float,
    val wind_speed: Float
): Serializable