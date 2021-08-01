package com.example.weather.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.data.Daily
import com.example.weather.data.WeatherResponse
import com.example.weather.databinding.ItemWeatherDailyBinding
import com.example.weather.util.DateUtils.getFriendlyDateString
import com.example.weather.util.WeatherUtils.getLargeArtResourceIdForWeatherCondition
import com.example.weather.util.WeatherUtils.getStringForWeatherCondition

class WeatherAdapter(private val context: Context,private val itemClick: OnItemClickRV): RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private val differCallBack = object : DiffUtil.ItemCallback<Daily>() {
        override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    val differ = AsyncListDiffer(this,differCallBack)

    inner class WeatherViewHolder(val binding: ItemWeatherDailyBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(ItemWeatherDailyBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.binding.weatherTV.text = getStringForWeatherCondition(context,differ.currentList[position].weather[0].id.toInt())
        holder.binding.dateTV.text = getFriendlyDateString(context,differ.currentList[position].dt.toLong(),false)?.subSequence(0,5)
        val symbol = (0x00B0).toChar()
        holder.binding.maxTempTV.text = "${differ.currentList[position].temp.max}${symbol}"
        holder.binding.minTempTV.text = "${differ.currentList[position].temp.min}${symbol}"
        holder.binding.forecastLogo.setImageResource(getLargeArtResourceIdForWeatherCondition(differ.currentList[position].weather[0].id.toInt()))
        holder.binding.dailyCL.setOnClickListener {
            itemClick.onCLick(differ.currentList[position])
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    interface OnItemClickRV{
        fun onCLick(weather: Daily)
    }
}