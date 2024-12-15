package com.etang.twitterclone.pages.post

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.etang.twitterclone.R
import com.etang.twitterclone.viewmodel.PostViewModel

class CreatePostActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        val etPostContent = findViewById<EditText>(R.id.etPostContent)
        val btnPublishPost = findViewById<Button>(R.id.btnPublishPost)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        btnPublishPost.setOnClickListener {
            val content = etPostContent.text.toString()
            val userId = 4
            if (content.isNotEmpty()) {
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

        observeViewModel()
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