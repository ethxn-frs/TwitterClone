package com.etang.twitterclone.pages.conversations

import ConversationsAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.etang.twitterclone.R
import com.etang.twitterclone.network.dto.auth_dto.LoginResponseDto
import com.etang.twitterclone.pages.ProfileActivity
import com.etang.twitterclone.pages.conversations.ConversationsDetailsActivity
import com.etang.twitterclone.pages.conversations.CreateConversationActivity
import com.etang.twitterclone.pages.post.TimelineActivity
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.ConversationViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ConversationsActivity : AppCompatActivity(){
    private val viewModel: ConversationViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ConversationsAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversations)
        sessionManager = SessionManager(this)
        val userId = sessionManager.getUserId()


        val headerLayout = findViewById<ConstraintLayout>(R.id.headerLayout)
        val tvTitle = headerLayout.findViewById<TextView>(R.id.tvHeaderTitle)
        tvTitle.text = "Conversations"
        val ivSettings = headerLayout.findViewById<ImageView>(R.id.ivSettings)

        val userResponse: LoginResponseDto? =
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                intent.getSerializableExtra("USER_DATA", LoginResponseDto::class.java)
            }else{
                @Suppress("DEPRECATION")
                intent.getSerializableExtra("USER_DATA") as? LoginResponseDto
            }
        userResponse?.let {
            Toast.makeText(this, "Bienvenue${it.user.username} !", Toast.LENGTH_SHORT).show()
        }

        recyclerView = findViewById(R.id.recyclerViewConversations)
        adapter = ConversationsAdapter(
            context = this,
            conversations = emptyList(),
            sessionManager = sessionManager,
            onConversationClicked = {conversation ->
                Toast.makeText(
                    this,
                    "Ouverture de la conversation avec ${conversation.name}",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this, ConversationsDetailsActivity::class.java)
                intent.putExtra("CONVERSATION_ID", conversation.id)
                startActivity(intent)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val fabNewConversation = findViewById<FloatingActionButton>(R.id.fabNewConversations)
        fabNewConversation.setOnClickListener{
            val intent = Intent(this, CreateConversationActivity::class.java)
            startActivity(intent)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.navigation_home -> {
                    val intent = Intent(this, TimelineActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_messages -> {
                    true
                }
                else -> false
            }
        }
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchUserConversations(userId)
        }

        observeViewModel()
        viewModel.fetchUserConversations(userId)

        ivSettings.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    private fun observeViewModel(){
        viewModel.userConversations.observe(this){conversations ->
            adapter.submitList(conversations)
            swipeRefreshLayout.isRefreshing = false
        }
    }
}