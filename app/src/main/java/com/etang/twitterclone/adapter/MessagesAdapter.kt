package com.etang.twitterclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.etang.twitterclone.R
import com.etang.twitterclone.data.model.Message

class MessagesAdapter(
    private val currentUserId: Int,
    private val otherParticipantName: String
) : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>(){

    private var messages: List<Message> = listOf()

    inner class MessageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val authorNameTextView : TextView = itemView.findViewById(R.id.tvAuthorName)
        val messageContentTextView: TextView = itemView.findViewById(R.id.tvMessageContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_messages, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        val author = message.author
        holder.authorNameTextView.text = if (author != null){
            "${author.firstName} ${author.lastName}"
        }else{
            "Inconnu"
        }
        holder.messageContentTextView.text = message.content

        holder.itemView.setOnClickListener {
            if (author?.id == currentUserId){
                Toast.makeText(
                    holder.itemView.context,
                    "Envoyé à : $otherParticipantName",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                Toast.makeText(
                    holder.itemView.context,
                    "Reçu de : ${author?.firstName ?: "Inconnu"}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getItemCount(): Int = messages.size

    fun submitList(newList: List<Message>){
        messages = newList
        notifyDataSetChanged()
    }
}