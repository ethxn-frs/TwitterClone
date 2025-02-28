package com.etang.twitterclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.etang.twitterclone.R
import com.etang.twitterclone.data.model.SettingItem

class SettingsAdapter(private val settings: List<SettingItem>) :
    RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>() {

    inner class SettingsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.ivSettingIcon)
        val title: TextView = view.findViewById(R.id.tvSettingTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_setting, parent, false)
        return SettingsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        val setting = settings[position]
        holder.icon.setImageResource(setting.iconRes)
        holder.title.text = setting.title
    }

    override fun getItemCount() = settings.size
}