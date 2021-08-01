package com.example.weather.util

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtils {
    @SuppressLint("SimpleDateFormat")
    fun getFriendlyDateString(context: Context, normalizedUtcMidnight: Long, showFullDate: Boolean): String? {
        val sdf = SimpleDateFormat("MM/dd/yy hh:mm a")
        val netDate = Date(normalizedUtcMidnight * 1000)
        return sdf.format(netDate)
    }
}