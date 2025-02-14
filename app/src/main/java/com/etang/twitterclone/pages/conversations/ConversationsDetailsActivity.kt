package com.etang.twitterclone.pages.conversations

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.MessagesAdapter
import com.etang.twitterclone.data.model.Conversation
import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.ConversationViewModel
import com.etang.twitterclone.viewmodel.MessagesViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ConversationsDetailsActivity : AppCompatActivity() {

    private lateinit var recyclerViewMessages: RecyclerView
    private lateinit var messagesAdapter: MessagesAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var conversation: Conversation

    private lateinit var messageContent: EditText
    private lateinit var btnSendMessage: Button

    private val messagesViewModel: MessagesViewModel by viewModels()
    private val conversationViewModel: ConversationViewModel by viewModels()

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

        messageContent = findViewById(R.id.MessageContent)
        btnSendMessage = findViewById(R.id.btnSendMessage)

        btnSendMessage.setOnClickListener{
            val content = messageContent.text.toString().trim()
            if (content.isNotEmpty()){
                messagesViewModel.sendMessage(conversation.id, currentUserId, content)
                messageContent.text.clear()
            }else{
                Toast.makeText(this, "Veuillez saisir un message", Toast.LENGTH_SHORT).show()
            }
        }
        val conversationId = intent.getIntExtra("CONVERSATION_ID", -1)
        if(conversationId == -1){
            finish()
            return
        }

        observeConversationDetails()
        conversationViewModel.fetchConversationById(conversationId, currentUserId)

    }
    private fun observeConversationDetails(){
        conversationViewModel.conversationDetails.observe(this) { conv ->
            if(conv != null){
                conversation = conv
                displayConversationDetails()
                loadMessages()
            }else{
                Toast.makeText(this, "Failed to load conversation details", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        conversationViewModel.error.observe(this){errorMsg ->
            errorMsg?.let{
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeMessages(){
        messagesViewModel.messages.observe(this){ messages ->
            messagesAdapter.submitList(messages)
            recyclerViewMessages.scrollToPosition(messages.size - 1)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun displayConversationDetails(){
        findViewById<TextView>(R.id.tvConversationName).text = conversation.name
        findViewById<TextView>(R.id.tvCreatedAt).text = "Created at: ${formatDateTime(conversation.createdAt)}"

        val currentUserId = sessionManager.getUserId()
        val creator: User? = conversation.users.firstOrNull {it.id == currentUserId}
        val participants = conversation.users.joinToString(",") { it.username }
        findViewById<TextView>(R.id.tvCreator)?.text = "Créateur : ${creator?.username ?: "Inconnu"}"

        findViewById<TextView>(R.id.tvParticipants)?.text = "Participants : $participants"

        val sender: User? = conversation.users.firstOrNull { it.id != currentUserId }
        findViewById<TextView>(R.id.idUtilisateurSent)?.text = "Message envoyé à : ${sender?.firstName ?: "Inconnu"} ${sender?.lastName ?: ""}"

    }
    private fun loadMessages(){
        val sortedMessages = conversation.messages.sortedBy { it.sentAt }
        messagesAdapter.submitList(sortedMessages)
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