package com.etang.twitterclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.etang.twitterclone.R
import com.etang.twitterclone.data.model.SettingItem

class SettingsAdapter(private val items: List<SettingItem>) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val ivArrow: ImageView = itemView.findViewById(R.id.ivArrow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_setting, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.ivIcon.setImageResource(item.icon)
        holder.tvTitle.text = item.title
        holder.tvDescription.text = item.description

        // Afficher la description uniquement si elle est non vide
        holder.tvDescription.visibility = if (item.description.isNotEmpty()) View.VISIBLE else View.GONE

        // Ajouter un clic
        holder.itemView.setOnClickListener { item.action.invoke() }
    }

    override fun getItemCount(): Int = items.size
}