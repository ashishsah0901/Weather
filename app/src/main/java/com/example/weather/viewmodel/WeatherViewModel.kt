package com.example.weather.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weather.WeatherApplication
import com.example.weather.data.WeatherResponse
import com.example.weather.repository.WeatherRepository
import com.example.weather.util.Resource
import kotlinx.coroutines.launch

class WeatherViewModel(
        private val weatherRepository: WeatherRepository, application: Application
) : AndroidViewModel(application){
    val weatherResponse: MutableLiveData<Resource<WeatherResponse>> = MutableLiveData()
    var weather: WeatherResponse?= null
    fun getWeatherByLatLon(lat: Double, lon: Double, mode: String) = viewModelScope.launch {
        weatherResponse.postValue(Resource.Loading())
        if(hasInternetConnection()) {
            val response = weatherRepository.getWeatherByLatLon(lat, lon, mode)
            if (response.isSuccessful) {
                response.body()?.let {
                    weather = it
                    weatherResponse.postValue(Resource.Success(it))
                }
            } else {
                weatherResponse.postValue(Resource.Error(response.message()))
            }
        }else{
            weatherResponse.postValue(Resource.Error("No Internet Connection"))
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<WeatherApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)?: return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}