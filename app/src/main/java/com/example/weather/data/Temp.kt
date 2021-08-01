package com.example.weather.data

import java.io.Serializable

data class Temp(
    val day: Float,
    val eve: Float,
    val max: Float,
    val min: Float,
    val morn: Float,
    val night: Float
): Serializable