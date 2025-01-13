package com.etang.twitterclone.pages

import ConversationsAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etang.twitterclone.R
import com.etang.twitterclone.viewmodel.ConversationViewModel

class ConversationsActivity : AppCompatActivity() {

    private val viewModel: ConversationViewModel by viewModels()
    private lateinit var adapter: ConversationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversations)
        
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewConversations)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ConversationsAdapter(this, listOf())
        recyclerView.adapter = adapter

        observeViewModel()

        val userId = 18
        viewModel.fetchUserConversations(userId)
    }

    private fun observeViewModel() {
        viewModel.userConversations.observe(this) { conversations ->
            adapter.updateData(conversations)
        }

        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}
