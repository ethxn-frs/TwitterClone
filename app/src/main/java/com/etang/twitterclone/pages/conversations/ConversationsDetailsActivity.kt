package com.etang.twitterclone.pages.conversations

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.MessagesAdapter
import com.etang.twitterclone.data.model.Conversation
import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.ConversationViewModel
import com.etang.twitterclone.viewmodel.MessagesViewModel
import com.etang.twitterclone.viewmodel.UserViewModel
import kotlinx.coroutines.launch
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
    private val conversationViewModel: ConversationViewModel by viewModels {
        ConversationViewModelFactory(SessionManager(this))
    }
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var ivOptions: ImageView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversations_details)

        sessionManager = SessionManager(this)
        val currentUserId = sessionManager.getUserId()

        val conversationId = intent.getIntExtra("CONVERSATION_ID", -1)
        if(conversationId == -1){
            finish()
            return
        }

        val ivBack = findViewById<ImageView>(R.id.ivBack)
        ivBack.setOnClickListener{
            setResult(RESULT_OK)
            finish()
        }

        ivOptions = findViewById(R.id.ivOptions)
        ivOptions.setOnClickListener {
            showOptionsMenu(conversationId, currentUserId)
        }

        val headerLayout = findViewById<ConstraintLayout>(R.id.headerLayout)
        val tvHeaderTitle = headerLayout.findViewById<TextView>(R.id.tvHeaderTitle)
        tvHeaderTitle.text = "Message"

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages)
        messagesAdapter = MessagesAdapter(currentUserId, "Inconnu")
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        recyclerViewMessages.layoutManager = layoutManager
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

        observeConversationDetails()
        observeMessages()
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
            Log.d("MessagesDebug", "Messages affichés: ${messages.map { it.content }}")
            messagesAdapter.submitList(messages)
            recyclerViewMessages.post{
                recyclerViewMessages.scrollToPosition(messages.size - 1)
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun displayConversationDetails(){
        findViewById<TextView>(R.id.tvCreatedAt).text = "Created at: ${formatDateTime(conversation.createdAt)}"

        val conversationName = if (conversation.name.contains("function now")) {
            conversation.users.joinToString(", ") { it.username } // Liste des participants
        } else {
            conversation.name
        }

        findViewById<TextView>(R.id.tvConversationName).text = conversationName
        val currentUserId = sessionManager.getUserId()

        val creator: User? = conversation.users.firstOrNull {it.id == currentUserId}
        val participants = conversation.users.joinToString(",") { it.username }
        findViewById<TextView>(R.id.tvCreator)?.text = "Créateur : ${creator?.username ?: "Inconnu"}"

        findViewById<TextView>(R.id.tvParticipants)?.text = "Participants : $participants"
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

    private fun showOptionsMenu(conversationId: Int, currentUserId: Int){
        val popupMenu = PopupMenu(this, ivOptions)
        popupMenu.menuInflater.inflate(R.menu.menu_conversations_options, popupMenu.menu)

        val isCreator = conversation.users.any { it.id == currentUserId }

        if(!isCreator){
            popupMenu.menu.findItem(R.id.menu_remove_user).isVisible = false
        }

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when(item.itemId){
                R.id.menu_add_user -> {
                    showAddUserDialog(conversationId)
                    true
                }
                R.id.menu_remove_user -> {
                    showRemoveUserDialog(conversationId)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showAddUserDialog(conversationId: Int){
        val input = EditText(this)
        input.hint = "Nom d'utilisateur"

        AlertDialog.Builder(this)
            .setTitle("Ajouter un utilisateur")
            .setMessage("Entrer le nom d'utilisateur à ajouter")
            .setView(input)
            .setPositiveButton("Ajouter") { _, _ ->
                val username = input.text.toString().trim()
                if(username.isNotEmpty()){
                    addUserToConversation(conversationId, username)
                }else{
                    Toast.makeText(this, "Veuillez entrer un nom d'utilisateur max", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    private fun showRemoveUserDialog(conversationId: Int) {
        val participants = conversation.users.map { it.username }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Supprimer un utilisateur")
            .setSingleChoiceItems(participants, -1) { dialog, which ->
                val selectedUser = conversation.users[which]
                removeUserFromConversation(conversationId, selectedUser.id)
                dialog.dismiss()
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    private fun addUserToConversation(conversationId: Int, username: String) {
        lifecycleScope.launch {
            try {
                val user = userViewModel.searchUserByName(username)
                if (user != null) {
                    conversationViewModel.addUserToConversation(conversationId, user.id, username)
                    Toast.makeText(this@ConversationsDetailsActivity, "$username a été ajouté", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ConversationsDetailsActivity, "Utilisateur introuvable", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ConversationsDetailsActivity, "Erreur : ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun removeUserFromConversation(conversationId: Int, userId: Int) {
        lifecycleScope.launch {
            try {
                conversationViewModel.removeUserFromConversation(conversationId, userId)
                Toast.makeText(this@ConversationsDetailsActivity, "Utilisateur supprimé", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@ConversationsDetailsActivity, "Erreur : ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}