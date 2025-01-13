package com.etang.twitterclone.pages

import ConversationsAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etang.twitterclone.R
import com.etang.twitterclone.network.RetrofitClient
import com.etang.twitterclone.network.services.ConversationDataService
import com.etang.twitterclone.repositories.ConversationRepository
import com.etang.twitterclone.viewmodel.ConversationViewModel
import com.etang.twitterclone.viewmodel.ConversationViewModelFactory
import com.etang.twitterclone.network.services.RetrofitInstance

class ConversationsActivity : AppCompatActivity() {

    private lateinit var viewModel: ConversationViewModel
    private lateinit var adapter: ConversationsAdapter
    private lateinit var conversationService: ConversationDataService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversations)

        // Initialiser ConversationDataService depuis Retrofit
        conversationService = RetrofitClient.conversationDataService

        // Initialiser Repository et ViewModel
        val repository = ConversationRepository(conversationService)
        val factory = ConversationViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(ConversationViewModel::class.java)

        // Configurer Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // Configurer RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewConversations)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ConversationsAdapter(this, listOf())
        recyclerView.adapter = adapter

        // Observer les données du ViewModel
        observeViewModel()

        // Charger les conversations de l'utilisateur
        val userId = 1 // Vous pouvez récupérer cet ID depuis SessionManager ou autre.
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
