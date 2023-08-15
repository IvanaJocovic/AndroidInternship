package com.ivanajocovic.weather.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivanajocovic.weather.R

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun setData(
        cityName: String,
        onDelete: (String) -> Unit,
        onClick: (String) -> Unit
    ) {
        itemView.findViewById<TextView>(R.id.itemWeatherTempTxt).text = cityName
        itemView.findViewById<ImageView>(R.id.imgDelete).setOnClickListener {
            onDelete.invoke(cityName)
        }
        itemView.setOnClickListener {
            onClick.invoke(cityName)
        }
    }
}