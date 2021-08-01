package com.example.weather.api

import com.example.weather.data.WeatherResponse
import com.example.weather.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("data/2.5/onecall?")
    suspend fun getWeatherDataByLatLon(
            @Query("lat")
            lat: Double = 21.15,
            @Query("lon")
            lon: Double = 79.1,
            @Query("units")
            type: String = "metric",
            @Query("exclude")
            exclude: String = "minutely",
            @Query("appid")
            app_id: String = API_KEY
    ): Response<WeatherResponse>
}