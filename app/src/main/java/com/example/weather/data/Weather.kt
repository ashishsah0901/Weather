package com.example.weather.data

import java.io.Serializable

data class Weather(
    val description: String,
    val icon: String,
    val id: Float,
    val main: String
): Serializable