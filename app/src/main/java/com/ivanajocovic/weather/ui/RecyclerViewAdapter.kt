package com.ivanajocovic.weather.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivanajocovic.weather.R

class RecyclerViewAdapter(
    private var data: List<String>,
    private val onDelete: (String) -> Unit,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.setData(
            cityName = item,
            onDelete = onDelete,
            onClick = onClick
        )
    }

    override fun getItemCount(): Int = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<String>) {
        data = newData
        notifyDataSetChanged()
    }
}





