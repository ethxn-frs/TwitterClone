package com.etang.twitterclone.pages.conversations

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.MessagesAdapter
import com.etang.twitterclone.data.model.Conversation
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.ConversationViewModel
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ConversationsDetailsActivity : AppCompatActivity() {

    private lateinit var recyclerViewMessages: RecyclerView
    private lateinit var messagesAdapter: MessagesAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var conversation: Conversation

    private val viewModel: ConversationViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversations_details)

        val headerLayout = findViewById<ConstraintLayout>(R.id.headerLayout)
        val tvHeaderTitle = headerLayout.findViewById<TextView>(R.id.tvHeaderTitle)
        tvHeaderTitle.text = "Message"
        sessionManager = SessionManager(this)
        val currentUserId = sessionManager.getUserId()

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages)
        messagesAdapter = MessagesAdapter(currentUserId, "Inconnu")
        recyclerViewMessages.layoutManager = LinearLayoutManager(this)
        recyclerViewMessages.adapter = messagesAdapter

        val conversationId = intent.getIntExtra("CONVERSATION_ID", -1)
        if(conversationId == -1){
            finish()
            return
        }

        observeConversationDetails()
        viewModel.fetchConversationById(conversationId, currentUserId)

    }
    private fun observeConversationDetails(){
        viewModel.conversationDetails.observe(this) {conv ->
            if(conv != null){
                conversation = conv
                displayConversationDetails()
                loadMessages()
            }else{
                Toast.makeText(this, "Failed to load conversation details", Toast.LENGTH_SHORT).show()
                finish()
            }


        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayConversationDetails(){
        findViewById<TextView>(R.id.tvConversationName).text = conversation.name

        val participants = conversation.users.joinToString(",") { it.username }
        findViewById<TextView>(R.id.tvParticipants).text = "Participants: $participants"

        findViewById<TextView>(R.id.tvCreatedAt).text = "Created at: ${formatDateTime(conversation.createdAt)}"

        val currentUserId = sessionManager.getUserId()
        val userIdSent = conversation.users.firstOrNull{ it.id != currentUserId}
        findViewById<TextView>(R.id.idUtilisateurSent).text =
            "Message envoyé à : ${userIdSent?.firstName} ${userIdSent?.lastName}"

        messagesAdapter = MessagesAdapter(currentUserId, userIdSent?.firstName ?: "Inconnu")
        recyclerViewMessages.adapter = messagesAdapter
    }
    private fun loadMessages(){
        messagesAdapter.submitList(conversation.messages)
    }

    private fun formatDateTime(createdAt: String): String{
        return try{
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(createdAt)
            val outputFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            outputFormat.format(date)
        }catch (e: Exception){
            "Unknown"
        }
    }
}