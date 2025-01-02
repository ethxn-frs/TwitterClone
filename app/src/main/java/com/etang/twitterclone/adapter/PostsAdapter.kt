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
    private val onLikeClicked: (postId: Int) -> Unit,
    private val onShareClicked: (post: Post) -> Unit
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
        holder.bind(posts[position], currentUserId, onLikeClicked, onShareClicked)
    }

    override fun getItemCount(): Int = posts.size

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
        private val tvAuthorUsername: TextView = itemView.findViewById(R.id.tvAuthorUsername)
        private val tvContent: TextView = itemView.findViewById(R.id.tvContent)
        private val tvLikes: TextView = itemView.findViewById(R.id.tvLikes)
        private val tvComments: TextView = itemView.findViewById(R.id.tvComments)
        private val tvTimeAgo: TextView = itemView.findViewById(R.id.tvTimeAgo)
        private val btnLike: ImageButton = itemView.findViewById(R.id.btnLike)
        private val btnShare: ImageButton = itemView.findViewById(R.id.btnShare)

        @SuppressLint("SetTextI18n")
        fun bind(
            post: Post,
            currentUserId: Int,
            onLikeClicked: (postId: Int) -> Unit,
            onShareClicked: (post: Post) -> Unit
        ) {
            tvAuthor.text = "${post.author.firstName} ${post.author.lastName}"
            tvAuthorUsername.text = "@${post.author.username}"
            tvContent.text = post.content
            tvLikes.text = "${post.userHaveLiked.size}"
            tvComments.text = "${post.comments.size}"
            tvTimeAgo.text = "· ${formatTimeAgo(post.createdAt)}"

            val isLiked = post.userHaveLiked.any { it.id == currentUserId }
            updateLikeButtonIcon(isLiked)

            btnLike.setOnClickListener {
                onLikeClicked(post.id)
                val newIsLiked = !isLiked
                updateLikeButtonIcon(newIsLiked)
            }

            btnShare.setOnClickListener {
                onShareClicked(post)
            }

        }

        private fun updateLikeButtonIcon(isLiked: Boolean) {
            if (isLiked) {
                btnLike.setImageResource(R.drawable.ic_favorite_filled24px)
            } else {
                btnLike.setImageResource(R.drawable.ic_favorite_outlined24px)
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
