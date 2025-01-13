package com.etang.twitterclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.etang.twitterclone.R
import com.etang.twitterclone.data.model.Message
import com.bumptech.glide.Glide
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter

class MessagesAdapter : ListAdapter<Message, MessagesAdapter.MessageViewHolder>(
    object : DiffUtil.ItemCallback<Message>(){
        override fun areItemsTheSame(oldItem: Message, newItem: Message) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Message, newItem: Message) = oldItem == newItem

    }
) {
    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val profileImage: ImageView = view.findViewById(R.id.ivProfileImage)
        val senderName: TextView = view.findViewById(R.id.tvProfileName)
        val lastMessage: TextView = view.findViewById(R.id.lastname)
        val messageDate: TextView = view.findViewById(R.id.welcomeMessage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_messages, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = getItem(position)
        holder.senderName.text = message.author.firstName
        holder.lastMessage.text = message.content
        holder.messageDate.text = message.sentAt.toString()

        Glide.with(holder.profileImage.context)
            .load(message.author) // ajoutez l'image de l'author
            .placeholder(R.drawable.ic_profile)
            .into(holder.profileImage)
    }
}