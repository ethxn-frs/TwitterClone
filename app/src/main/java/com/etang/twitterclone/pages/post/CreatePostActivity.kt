package com.etang.twitterclone.pages.post

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.etang.twitterclone.R
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.PostViewModel

class CreatePostActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

            sessionManager = SessionManager(this)

        val etPostContent = findViewById<EditText>(R.id.etPostContent)
        val btnPublishPost = findViewById<Button>(R.id.btnPublishPost)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        observeViewModel()

        btnPublishPost.setOnClickListener {
            val content = etPostContent.text.toString()
            val userId = sessionManager.getUserId();
            if (content.isNotEmpty()) {
                Log.d("create post","création de post")
                viewModel.createPost(userId, content, null)
            } else {
                Toast.makeText(this, "Post cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurer la Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Gérer le clic sur la flèche de retour
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {
        viewModel.postSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Post published!", Toast.LENGTH_SHORT).show()
                finish() // Ferme l'écran après publication
            } else {
                Toast.makeText(this, "Failed to publish post", Toast.LENGTH_SHORT).show()
            }
        }
    }

}