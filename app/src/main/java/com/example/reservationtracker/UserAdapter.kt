package com.example.reservationtracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter (private val mList: List<UserData>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_template, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]
        holder.itemName.text = item.name
        holder.itemTable.text = item.tSize
        holder.itemTime.text = item.time
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemName: TextView = itemView.findViewById(R.id.utempName)
        var itemTable: TextView = itemView.findViewById(R.id.utempTable)
        var itemTime: TextView = itemView.findViewById(R.id.utempTime)
    }
}