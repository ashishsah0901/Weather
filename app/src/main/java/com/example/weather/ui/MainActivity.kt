package com.example.weather.ui

import android.content.Intent
import android.content.SharedPreferences
import android.location.Geocoder
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.example.weather.R
import com.example.weather.adapter.WeatherAdapter
import com.example.weather.adapter.WeatherHourlyAdapter
import com.example.weather.data.Daily
import com.example.weather.data.WeatherResponse
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.repository.WeatherRepository
import com.example.weather.services.WeatherServiceForeground
import com.example.weather.services.WeatherWorker
import com.example.weather.util.DateUtils.getFriendlyDateString
import com.example.weather.util.Resource
import com.example.weather.util.WeatherUtils.getLargeArtResourceIdForWeatherCondition
import com.example.weather.util.WeatherUtils.getStringForWeatherCondition
import com.example.weather.viewmodel.WeatherViewModel
import com.example.weather.viewmodel.WeatherViewModelProviderFactory
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), WeatherAdapter.OnItemClickRV, SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var hAdapter: WeatherHourlyAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: WeatherAdapter
    private var data: WeatherResponse ?= null
    private var isDone = false
    private var intentWeather: Intent ? =null
    companion object {
        lateinit var mViewModel: WeatherViewModel
    }
    private var mLat: Double = 21.15
    private var mLon: Double = 79.1
    private var mLocation: String = "Nagpur"
    private lateinit var geoCoder: Geocoder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)
        geoCoder = Geocoder(this, Locale.getDefault())
        hAdapter = WeatherHourlyAdapter(this)
        mAdapter = WeatherAdapter(this, this)
        binding.weatherHourlyRv.adapter = hAdapter
        binding.weatherHourlyRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.weatherRV.adapter = mAdapter
        binding.weatherRV.layoutManager = LinearLayoutManager(this)
        mLocation = PreferenceManager.getDefaultSharedPreferences(this).getString("location","Nagpur").toString()
        val answer = geoCoder.getFromLocationName(mLocation, 1)
        mLat = answer[0].latitude
        mLon = answer[0].longitude
        val repository = WeatherRepository()
        val weatherViewModelProviderFactory = WeatherViewModelProviderFactory(application,repository)
        mViewModel = ViewModelProvider(this,weatherViewModelProviderFactory).get(WeatherViewModel::class.java)
        mViewModel.weatherResponse.observe(this, {
            if(!isDone && intentWeather!=null) {
                stopService(intentWeather)
                isDone = true
            }
            when (it) {
                is Resource.Success -> {
                    data = it.data
                    showUI(it.data!!)
                }
                is Resource.Error -> {
                    errorMessage(it.message!!)
                }
                is Resource.Loading -> {
                    loading()
                }
            }
        })

        data = mViewModel.weatherResponse.value?.data
        if( data != null) {
            showUI(data!!)
        } else {
            if(PreferenceManager.getDefaultSharedPreferences(this).getString("units","metric")=="metric")
                mViewModel.getWeatherByLatLon(mLat, mLon,"metric")
            else
                mViewModel.getWeatherByLatLon(mLat, mLon,"imperial")
        }
        val constrains = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).setRequiresBatteryNotLow(true).setTriggerContentUpdateDelay(1, TimeUnit.HOURS).build()
        } else {
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).setRequiresBatteryNotLow(true).build()
        }
        val workManager = WorkManager.getInstance(applicationContext)
        val request = PeriodicWorkRequest.Builder(WeatherWorker::class.java, 3, TimeUnit.HOURS).setConstraints(constrains).build()
        workManager.enqueueUniquePeriodicWork("Background-Task-Repeated",ExistingPeriodicWorkPolicy.KEEP,request)
    }

    private fun loading() {
        binding.dateMainTV.visibility = View.GONE
        binding.weatherIV.visibility = View.GONE
        binding.mainDescriptionTV.visibility = View.GONE
        binding.currentTempTV.visibility = View.GONE
        binding.errorMessage.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.weatherRV.visibility = View.GONE
        binding.weatherHourlyRv.visibility = View.GONE
    }

    private fun errorMessage(message: String) {
        binding.dateMainTV.visibility = View.GONE
        binding.weatherIV.visibility = View.GONE
        binding.mainDescriptionTV.visibility = View.GONE
        binding.currentTempTV.visibility = View.GONE
        binding.errorMessage.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.errorMessage.text = message
        binding.weatherRV.visibility = View.GONE
        binding.weatherHourlyRv.visibility = View.GONE
    }

    private fun showUI(data: WeatherResponse) {
        binding.dateMainTV.visibility = View.VISIBLE
        binding.weatherIV.visibility = View.VISIBLE
        binding.mainDescriptionTV.visibility = View.VISIBLE
        binding.currentTempTV.visibility = View.VISIBLE
        binding.errorMessage.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.weatherRV.visibility = View.VISIBLE
        binding.weatherHourlyRv.visibility = View.VISIBLE
        binding.dateMainTV.text = getFriendlyDateString(this,data.current.dt.toLong(),true)?.subSequence(0,5)
        val symbol = (0x00B0).toChar()
        binding.currentTempTV.text = "${data.current.temp}$symbol"
        binding.mainDescriptionTV.text = getStringForWeatherCondition(this, data.current.weather[0].id.toInt())
        binding.weatherIV.setImageResource(getLargeArtResourceIdForWeatherCondition(data.current.weather[0].id.toInt()))
        mAdapter.differ.submitList(data.daily)
        hAdapter.differ.submitList(data.hourly)
    }

    override fun onCLick(weather: Daily) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("data", weather)
        startActivity(intent)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        isDone = false
        intentWeather = Intent(this, WeatherServiceForeground::class.java)
        startService(intentWeather)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.settings_menu)
            startActivity(Intent(this, SettingsActivity::class.java))
        return super.onOptionsItemSelected(item)
    }
}
