package com.example.weather.repository

import android.content.Context
import android.location.Geocoder
import com.example.weather.api.RetrofitInstance
import com.example.weather.data.WeatherResponse
import retrofit2.Response
import java.util.*

class WeatherRepository {
    suspend fun getWeatherByLatLon(lat: Double, lon: Double,mode: String): Response<WeatherResponse> {
        return RetrofitInstance.api.getWeatherDataByLatLon(lat,lon,mode)
    }
}