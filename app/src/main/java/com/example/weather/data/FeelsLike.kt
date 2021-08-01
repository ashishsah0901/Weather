package com.example.weather.data

import java.io.Serializable

data class FeelsLike(
    val day: Float,
    val eve: Float,
    val morn: Float,
    val night: Float
): Serializable