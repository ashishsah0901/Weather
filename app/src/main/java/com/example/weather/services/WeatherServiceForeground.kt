package com.example.weather.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import com.example.weather.ui.MainActivity
import com.example.weather.ui.MainActivity.Companion.mViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class WeatherServiceForeground : Service() {

    private val channelID = "ForegroundService Kotlin"

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundService()
        val location = PreferenceManager.getDefaultSharedPreferences(this).getString("location","Nagpur").toString()
        val geoCoder = Geocoder(this, Locale.getDefault())
        val latitudeLongitude = geoCoder.getFromLocationName(location, 1)
        val metric = PreferenceManager.getDefaultSharedPreferences(this).getString("units","metric")=="metric"
        CoroutineScope(Dispatchers.IO).launch {
            if(metric)
                mViewModel.getWeatherByLatLon(latitudeLongitude[0].latitude, latitudeLongitude[0].longitude,"metric")
            else
                mViewModel.getWeatherByLatLon(latitudeLongitude[0].latitude, latitudeLongitude[0].longitude,"imperial")
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }
        val pendingIntent = PendingIntent.getActivity(this,0,Intent(this, MainActivity::class.java),PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(this,channelID)
                .setAutoCancel(true)
                .setContentTitle("Downloading...")
                .setContentIntent(pendingIntent)
                .setContentIntent(pendingIntent)
                .build()
        startForeground(1,notification)
    }

    private fun createNotificationChannel(manager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(channelID, "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}