package com.example.weather.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.weather.R
import com.example.weather.data.Daily
import com.example.weather.databinding.ActivityDetainBinding
import com.example.weather.util.DateUtils.getFriendlyDateString
import com.example.weather.util.WeatherUtils
import com.example.weather.util.WeatherUtils.getFormattedWind
import com.example.weather.util.WeatherUtils.getLargeArtResourceIdForWeatherCondition
import com.example.weather.util.WeatherUtils.getStringForWeatherCondition

class DetailActivity : AppCompatActivity() {
    private lateinit var weather: Daily
    private lateinit var binding: ActivityDetainBinding
    private var showLess = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        weather = intent.getSerializableExtra("data") as Daily
        binding.detailDateTV.text = getFriendlyDateString(this, weather.dt.toLong(),true)?.subSequence(0,5)
        binding.mainDescriptionTV.text = getStringForWeatherCondition(this,weather.weather[0].id.toInt())
        val symbol = (0x00B0).toChar()
        binding.currentTempTV.text = "${weather.temp.max}$symbol"
        binding.tempTV.text = "${weather.temp.min}$symbol"
        binding.humidityTV.text ="${weather.humidity} %"
        binding.moonRiseTV.text = getFriendlyDateString(this, weather.moonrise.toLong(),true)?.subSequence(9,17)
        binding.moonSetTV.text = getFriendlyDateString(this, weather.moonset.toLong(),true)?.subSequence(9,17)
        binding.pressureTV.text = "${weather.pressure} hPa"
        binding.rainTV.text = "${weather.rain}mm"
        binding.rainProbTV.text = "${weather.pop} %"
        binding.sunRiseTV.text = getFriendlyDateString(this, weather.sunrise.toLong(),true)?.subSequence(9,17)
        binding.sunSetTV.text = getFriendlyDateString(this, weather.sunset.toLong(),true)?.subSequence(9,17)
        binding.windTV.text = getFormattedWind(this,weather.wind_speed,weather.wind_deg)
        binding.detailWeatherIV.setImageResource(getLargeArtResourceIdForWeatherCondition(weather.weather[0].id.toInt()))
        binding.detailDescriptionTV.text = WeatherUtils.getStringForWeatherCondition(this, weather.weather[0].id.toInt())
        binding.showLessMore.setOnClickListener {
            toggleView()
        }
    }

    private fun share() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,weather.toString())
        val chooser = Intent.createChooser(intent,"Share the weather")
        startActivity(chooser)
    }

    private fun toggleView() {
        if(showLess) {
            binding.detailDescriptionTV.visibility = View.VISIBLE
            binding.windCL.visibility = View.VISIBLE
            binding.sunRiseCL.visibility = View.VISIBLE
            binding.sunSetCL.visibility = View.VISIBLE
            binding.rainProbCL.visibility = View.VISIBLE
            binding.showLessMore.text = "SHOW LESS"
            showLess = false
        } else {
            binding.detailDescriptionTV.visibility = View.GONE
            binding.windCL.visibility = View.GONE
            binding.sunRiseCL.visibility = View.GONE
            binding.sunSetCL.visibility = View.GONE
            binding.rainProbCL.visibility = View.GONE
            binding.showLessMore.text = "SHOW MORE"
            showLess = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.share_menu)
            share()
        return super.onOptionsItemSelected(item)
    }
}