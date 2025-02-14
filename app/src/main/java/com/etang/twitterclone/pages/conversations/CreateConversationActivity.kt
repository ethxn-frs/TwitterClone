package com.etang.twitterclone.pages.conversations

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.etang.twitterclone.R
import com.etang.twitterclone.repositories.UserRepository
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.ConversationViewModel
import com.etang.twitterclone.viewmodel.PostViewModel
import kotlinx.coroutines.launch

class CreateConversationActivity : AppCompatActivity() {

    private val viewModel: ConversationViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private val usersRepository = UserRepository()

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
            val creatorId = sessionManager.getUserId()
            if(participantsText.isNotEmpty()){
                val usernames = participantsText.split(",").map {it.trim()}
                viewModel.createConversation(creatorId, usernames)

            }else{
                Toast.makeText(this, "Veuillez saisir au moins un username", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun observeViewModel() {
        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let{
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.creationSuccess.observe(this) {success ->
            if(success){
                Toast.makeText(this, "Creation en cours", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }

}