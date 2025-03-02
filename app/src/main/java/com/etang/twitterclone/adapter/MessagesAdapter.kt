package com.etang.twitterclone.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.etang.twitterclone.R
import com.etang.twitterclone.data.model.Message
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class MessagesAdapter(
    private val currentUserId: Int
) : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>(){

    companion object{
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }
    private var messages: List<Message> = listOf()

    inner class MessageViewHolder(itemView : View, val viewType: Int) : RecyclerView.ViewHolder(itemView){
        val authorNameTextView: TextView? = if (viewType == VIEW_TYPE_RECEIVED){
            itemView.findViewById(R.id.tvAuthorName)
        }else{
            null
        }
        val messageContentTextView: TextView = itemView.findViewById(R.id.messageContent)
        val tvMessageInfo: TextView = itemView.findViewById(R.id.tvMessageInfo)
        val tvSeenStatus: TextView = itemView.findViewById(R.id.tvSeenStatus)
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.author?.id == currentUserId) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutId = if (viewType == VIEW_TYPE_SENT){
            R.layout.item_messages_sent
        }else{
            R.layout.item_message_received
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return MessageViewHolder(
            view,
            viewType = viewType
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        if (holder.viewType == VIEW_TYPE_RECEIVED){
            holder.authorNameTextView?.text = message.author?.let {
                "${it.firstName} ${it.lastName}"
            } ?: "Inconnu"
        }
        val formattedDate = formatDateTime(message.sentAt)
        val authorName = message.author?.username ?: "Inconnu"

        holder.tvMessageInfo?.text = "$authorName ($formattedDate)"
        holder.messageContentTextView.text = message.content

        val totalParticipantsWhoSeenMessage = message.seenBy.count {it.id != message.author.id}
        val otherParticipants = message.author.username.count()

        holder.tvSeenStatus.text = when {
            totalParticipantsWhoSeenMessage == 0 -> ""
            totalParticipantsWhoSeenMessage == otherParticipants -> "Vu par tout le monde"
            totalParticipantsWhoSeenMessage > 0 -> "Vu par un utilisateur"
            else -> ""
        }

    }

    override fun getItemCount(): Int = messages.size

    fun submitList(newList: List<Message>){
        messages = newList
        notifyDataSetChanged()
    }

    private fun formatDateTime(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(dateString)
            val outputFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            outputFormat.format(date)
        } catch (e: Exception) {
            "Inconnu"
        }
    }
}