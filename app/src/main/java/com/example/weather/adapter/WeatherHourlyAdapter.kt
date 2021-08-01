package com.example.weather.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.data.Hourly
import com.example.weather.databinding.ItemWeatherHourlyBinding
import com.example.weather.util.DateUtils.getFriendlyDateString

class WeatherHourlyAdapter(private val context: Context): RecyclerView.Adapter<WeatherHourlyAdapter.WeatherHourlyViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Hourly>() {
        override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    val differ = AsyncListDiffer(this,differCallback)

    inner class WeatherHourlyViewHolder(val binding: ItemWeatherHourlyBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHourlyViewHolder {
        return WeatherHourlyViewHolder(ItemWeatherHourlyBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: WeatherHourlyViewHolder, position: Int) {
        holder.binding.textViewHourly.text = getFriendlyDateString(context,differ.currentList[position].dt.toLong(),false)?.subSequence(9,17)
        val symbol = (0x00B0).toChar()
        holder.binding.textViewTemp.text = "${differ.currentList[position].temp}${symbol}"
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}