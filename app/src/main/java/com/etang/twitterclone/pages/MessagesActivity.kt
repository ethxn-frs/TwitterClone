package com.etang.twitterclone.pages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.MessagesAdapter
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.MessagesViewModel

class MessagesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : MessagesAdapter
    private lateinit var viewModel: MessagesViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        sessionManager = SessionManager(this)
        val currentUserId = sessionManager.getUserId()

        val otherParticipantName = intent.getStringExtra("OTHER_PARTICIPANT") ?: "Inconnu"

        recyclerView = findViewById(R.id.recyclerViewMessages)
        adapter = MessagesAdapter(currentUserId, otherParticipantName)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(this).get(MessagesViewModel::class.java)

        viewModel.messages.observe(this){
            messages -> adapter.submitList(messages)
        }


    }
}