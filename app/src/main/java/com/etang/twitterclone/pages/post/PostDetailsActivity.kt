package com.etang.twitterclone.pages.post

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.PostsAdapter
import com.etang.twitterclone.data.model.Post
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.PostViewModel

class PostDetailsActivity : AppCompatActivity() {

    private lateinit var recyclerViewComments: RecyclerView
    private lateinit var commentsAdapter: PostsAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var post: Post
    private lateinit var btnLike: ImageButton
    private lateinit var tvLikes: TextView
    private var isLiked: Boolean = false
    private val viewModel: PostViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)

        sessionManager = SessionManager(this)

        val postId = intent.getIntExtra("POST_ID", -1)
        if (postId == -1) {
            finish()
            return
        }

        recyclerViewComments = findViewById(R.id.recyclerViewComments)
        commentsAdapter = PostsAdapter(
            sessionManager,
            onLikeClicked = { postId -> likeComment(postId) },
            onShareClicked = { comment -> shareComment(comment) }
        )
        recyclerViewComments.layoutManager = LinearLayoutManager(this)
        recyclerViewComments.adapter = commentsAdapter

        btnLike = findViewById(R.id.btnLike)
        tvLikes = findViewById(R.id.tvLikes)

        observePostDetails()
        viewModel.fetchPostById(postId)
    }

    private fun observePostDetails() {
        viewModel.postDetails.observe(this) { postDetails ->
            if (postDetails != null) {
                post = postDetails
                displayPostDetails()
                loadComments()
            } else {
                Toast.makeText(this, "Failed to load post details.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayPostDetails() {
        findViewById<TextView>(R.id.tvAuthor).text =
            post.author.firstName + " " + post.author.lastName
        findViewById<TextView>(R.id.tvAuthorUsername).text = "@${post.author.username}"
        findViewById<TextView>(R.id.tvContent).text = post.content
        tvLikes.text = "${post.userHaveLiked.size}"

        isLiked = post.userHaveLiked.any { it.id == sessionManager.getUserId() }
        updateLikeButtonIcon(isLiked)

        btnLike.setOnClickListener {
            isLiked = !isLiked
            updateLikeButtonIcon(isLiked)
            likePost(post.id)
            val currentLikes = tvLikes.text.toString().toInt()
            tvLikes.text = if (isLiked) "${currentLikes + 1}" else "${currentLikes - 1}"
        }
    }

    private fun loadComments() {
        commentsAdapter.submitList(post.comments)
    }

    private fun likeComment(postId: Int) {
        // Implémenter la logique pour liker un commentaire
        // Par exemple, appeler une API pour liker le commentaire
    }

    private fun shareComment(comment: Post) {
        // Implémenter la logique pour partager un commentaire
    }

    private fun updateLikeButtonIcon(isLiked: Boolean) {
        if (isLiked) {
            btnLike.setImageResource(R.drawable.ic_favorite_filled24px)
        } else {
            btnLike.setImageResource(R.drawable.ic_favorite_outlined24px)
        }
    }

    private fun likePost(postId: Int) {
        viewModel.likePost(postId, sessionManager.getUserId())
        observeLikeSuccess()
    }

    private fun observeLikeSuccess() {
        viewModel.likeSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Post liked successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to like the post.", Toast.LENGTH_SHORT).show()
                // Revenir à l'état initial en cas d'erreur
                isLiked = !isLiked
                updateLikeButtonIcon(isLiked)
            }
        }
    }
}