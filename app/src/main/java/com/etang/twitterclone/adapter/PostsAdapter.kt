package com.etang.twitterclone.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.etang.twitterclone.R
import com.etang.twitterclone.data.model.Post
import com.etang.twitterclone.session.SessionManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class PostsAdapter(
    private val sessionManager: SessionManager,
    private val onLikeClicked: (postId: Int) -> Unit
) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    private val posts = mutableListOf<Post>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newPosts: List<Post>) {
        posts.clear()
        posts.addAll(newPosts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentUserId = sessionManager.getUserId();
        holder.bind(posts[position], currentUserId) { postId ->
            onLikeClicked(postId)
        }
    }

    override fun getItemCount(): Int = posts.size

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
        private val tvAuthorUsername: TextView = itemView.findViewById(R.id.tvAuthorUsername)
        private val tvContent: TextView = itemView.findViewById(R.id.tvContent)
        private val tvLikes: TextView = itemView.findViewById(R.id.tvLikes)
        private val btnLike: ImageButton = itemView.findViewById(R.id.btnLike)
        private val tvTimeAgo: TextView = itemView.findViewById(R.id.tvTimeAgo)

        @SuppressLint("SetTextI18n")
        fun bind(post: Post, currentUserId: Int, onLikeClicked: (postId: Int) -> Unit) {
            tvAuthor.text = "${post.author.firstName} ${post.author.lastName}"
            tvAuthorUsername.text = "@${post.author.username}"
            tvContent.text = post.content
            tvLikes.text = "Likes: ${post.userHaveLiked.size}"
            tvTimeAgo.text = "· ${formatTimeAgo(post.createdAt)}"

            val isLiked = post.userHaveLiked.any { it.id == currentUserId }
            updateLikeButtonIcon(isLiked)

            btnLike.setOnClickListener {
                onLikeClicked(post.id)
                val newIsLiked = !isLiked
                updateLikeButtonIcon(newIsLiked)
            }
        }

        private fun updateLikeButtonIcon(isLiked: Boolean) {
            if (isLiked) {
                btnLike.setImageResource(R.drawable.baseline_heart_broken_24)
            } else {
                btnLike.setImageResource(R.drawable.outline_heart_broken_24)
            }
        }
    }

    fun formatTimeAgo(createdAt: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        return try {
            val createdDate = dateFormat.parse(createdAt)
            val now = Date()
            val diffInMillis = now.time - createdDate.time

            val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
            val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
            val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)

            when {
                seconds < 60 -> "$seconds seconds ago"
                minutes < 60 -> "$minutes minutes ago"
                hours < 24 -> "$hours hours ago"
                days < 7 -> "$days days ago"
                days < 30 -> "${days / 7} weeks ago"
                days < 365 -> "${days / 30} months ago"
                else -> "${days / 365} years ago"
            }
        } catch (e: Exception) {
            "unknown"
        }
    }
}
