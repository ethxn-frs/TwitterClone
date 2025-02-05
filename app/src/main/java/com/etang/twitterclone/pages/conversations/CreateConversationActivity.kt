package com.etang.twitterclone.pages.conversations

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.etang.twitterclone.R
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.ConversationViewModel
import com.etang.twitterclone.viewmodel.PostViewModel

class CreateConversationActivity : AppCompatActivity() {

    private val viewModel: ConversationViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_conversation)

        sessionManager = SessionManager(this)

        val etParticipants = findViewById<EditText>(R.id.etParticipants)
        val btnCreateConversation = findViewById<Button>(R.id.btnCreateConversation)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }
        observeViewModel()

        btnCreateConversation.setOnClickListener {
            val participantsText = etParticipants.text.toString()
            val userId = sessionManager.getUserId()
            if (participantsText.isNotEmpty()) {
                val participantsIds =
                    participantsText.split(",").mapNotNull { it.trim().toIntOrNull() }
                if (participantsIds.isNotEmpty()) {
                    viewModel.createConversation(userId, participantsIds)
                    Toast.makeText(this, "Conversation créée !", Toast.LENGTH_SHORT).show()
                    finish()

                } else {
                    Toast.makeText(this, "Veuillez entrer des IDs valides", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, "Veuillez entrer les IDs des participants", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }


    private fun observeViewModel() {
        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let{
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }


        }
    }

}