package com.example.gymtrovert.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtrovert.LoggedData
import com.example.gymtrovert.R


class ItemAdapter(private val context: Context, private val dataset: MutableList<LoggedData>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val loggedTimeTextView: TextView = view.findViewById(R.id.loggedTimeTextView)
        val loggedWeightTextView: TextView = view.findViewById(R.id.loggedWeightTextView)
        val numOfGymGoersTextView: TextView = view.findViewById(R.id.numOfGymGoersTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_history_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        Log.d("ay caramba", item.loggedTime +  item.loggedWeight)
        holder.loggedTimeTextView.text = item.loggedTime
        holder.loggedWeightTextView.text = item.loggedWeight + " lbs"
        holder.numOfGymGoersTextView.text = item.numOfGymGoers.toString() + " people"

    }
}