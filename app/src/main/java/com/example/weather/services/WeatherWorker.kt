package com.example.weather.services

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.preference.PreferenceManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weather.ui.MainActivity.Companion.mViewModel
import java.lang.Exception
import java.util.*

class WeatherWorker(private val context: Context, params: WorkerParameters) : Worker(context, params) {
    
    override fun doWork() : Result {
        Log.d("WeatherWorker","Syncing.......")
        return try {
            val location = PreferenceManager.getDefaultSharedPreferences(context).getString("location","Nagpur").toString()
            val geoCoder = Geocoder(context, Locale.getDefault())
            val latitudeLongitude = geoCoder.getFromLocationName(location, 1)
            val metric = PreferenceManager.getDefaultSharedPreferences(context).getString("units","metric")=="metric"
            if(metric)
                mViewModel.getWeatherByLatLon(latitudeLongitude[0].latitude, latitudeLongitude[0].longitude,"metric")
            else
                mViewModel.getWeatherByLatLon(latitudeLongitude[0].latitude, latitudeLongitude[0].longitude,"imperial")
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

}