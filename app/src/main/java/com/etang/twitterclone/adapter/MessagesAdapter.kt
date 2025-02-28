package com.etang.twitterclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.etang.twitterclone.R
import com.etang.twitterclone.data.model.Message

class MessagesAdapter(
    private val currentUserId: Int,
    private val otherParticipantName: String
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
        val btnReaction: ImageView = itemView.findViewById(R.id.btnReaction)
        val tvReaction: TextView = itemView.findViewById(R.id.tvReaction)
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
        holder.messageContentTextView.text = message.content

        holder.btnReaction.setOnClickListener { view ->
            showEmoji(view, holder, message)
        }

        holder.tvReaction.text = message.reaction ?: ""

    }

    private fun showEmoji(
        view: View?,
        holder: MessageViewHolder,
        message: Message
    ) {
        val popUpMenu = PopupMenu(view?.context, view)
        val emojis = listOf("👍", "❤️", "😂", "😮", "😢", "😡")
        for (emoji in emojis){
            popUpMenu.menu.add(emoji)
        }

        popUpMenu.setOnMenuItemClickListener { item->
            val selectedEmoji = item.title.toString()
            message.reaction = selectedEmoji
            holder.tvReaction.text = selectedEmoji
            notifyDataSetChanged()
            true
        }
        popUpMenu.show()

    }

    override fun getItemCount(): Int = messages.size

    fun submitList(newList: List<Message>){
        messages = newList
        notifyDataSetChanged()
    }
}