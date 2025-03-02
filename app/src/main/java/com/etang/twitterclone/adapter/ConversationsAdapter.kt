package com.etang.twitterclone.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.etang.twitterclone.R
import com.etang.twitterclone.data.model.Conversation
import com.etang.twitterclone.session.SessionManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class ConversationsAdapter(
    private val onConversationClicked: (Conversation) -> Unit,
    private val sessionManager: SessionManager
) : RecyclerView.Adapter<ConversationsAdapter.ConversationsViewHolder>() {

    private val conversations = mutableListOf<Conversation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_conversations, parent, false)
        return ConversationsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationsViewHolder, position: Int) {
        holder.bind(conversations[position])
    }

    override fun getItemCount(): Int = conversations.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<Conversation>) {
        conversations.clear()
        conversations.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ConversationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView)
        private val userNameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        private val lastMessageTextView: TextView = itemView.findViewById(R.id.lastMessageTextView)
        private val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)

        fun bind(conversation: Conversation) {
            val currentUserId = sessionManager.getUserId()

            val participants = conversation.users.filter { it.id != currentUserId }

            val conversationName = when (participants.size){
                1 -> participants.firstOrNull()?.username
                else -> participants.joinToString(", ") {it.username}

            }
            userNameTextView.text = conversationName

            // Charger la dernière conversation
            lastMessageTextView.text = if (conversation.messages.isNotEmpty()) {
                conversation.messages.last().content
            } else {
                "Aucun message"
            }

            timestampTextView.text = formatTimeAgo(conversation.createdAt)

            Glide.with(itemView.context)
                .load(R.drawable.ic_profile)
                .circleCrop()
                .into(profileImageView)

            itemView.setOnClickListener {
                onConversationClicked(conversation)
            }
        }
    }

    fun formatTimeAgo(createdAt: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        return try {
            val createdDate = dateFormat.parse(createdAt) ?: Date()
            val now = Date()
            val diffInMillis = now.time - createdDate.time

            when {
                diffInMillis < TimeUnit.MINUTES.toMillis(1) -> "${diffInMillis / 1000}s"
                diffInMillis < TimeUnit.HOURS.toMillis(1) -> "${diffInMillis / 1000 / 60}m"
                diffInMillis < TimeUnit.DAYS.toMillis(1) -> "${diffInMillis / 1000 / 60 / 60}h"
                diffInMillis < TimeUnit.DAYS.toMillis(7) -> "${diffInMillis / 1000 / 60 / 60 / 24}j"
                else -> SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(createdDate)
            }
        } catch (e: Exception) {
            "inconnu"
        }
    }

    fun updateConversation(conversation: Conversation?) {
        val index = conversations.indexOfFirst { it.id == conversation?.id  }
        if(index != -1){
            if (conversation != null) {
                conversations[index] = conversation
            }
            notifyItemChanged(index)
        }
    }
}