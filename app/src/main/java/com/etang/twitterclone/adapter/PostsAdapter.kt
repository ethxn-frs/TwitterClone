package com.etang.twitterclone.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.etang.twitterclone.R
import com.etang.twitterclone.data.model.Post
import com.etang.twitterclone.pages.post.PostDetailsActivity
import com.etang.twitterclone.repositories.PostRepository
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.PostViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        private val btnMoreActions: ImageButton = itemView.findViewById(R.id.btnMoreActions)
        private val btnLike: ImageButton = itemView.findViewById(R.id.btnLike)
        private val btnComment: ImageButton = itemView.findViewById(R.id.btnComment)
        private val btnShare: ImageButton = itemView.findViewById(R.id.btnShare)


        @SuppressLint("SetTextI18n")
        fun bind(
            post: Post,
            currentUserId: Int,
            onLikeClicked: (postId: Int) -> Unit,
            onShareClicked: (post: Post) -> Unit
        ) {
            println("Post ID: ${post.id}")
            tvAuthor.text = "${post.author.firstName} ${post.author.lastName}"
            tvAuthorUsername.text = "@${post.author.username}"
            tvContent.text = post.content
            tvLikes.text = "${post.userHaveLiked.size}"
            tvComments.text = "${post.comments.size}"
            tvTimeAgo.text = "· ${formatTimeAgo(post.createdAt)}"

            val isLiked = post.userHaveLiked.any { it.id == currentUserId }
            updateLikeButtonIcon(isLiked)

            btnMoreActions.setOnClickListener { view ->
                val popupMenu = PopupMenu(itemView.context, view)
                popupMenu.menuInflater.inflate(R.menu.menu_post_actions, popupMenu.menu)

                val authorMenu = popupMenu.menu.findItem(R.id.action_author_menu)
                authorMenu.title = post.author.username

                btnMoreActions.setOnClickListener { view ->
                    val popupMenu = PopupMenu(itemView.context, view)
                    popupMenu.menuInflater.inflate(R.menu.menu_post_actions, popupMenu.menu)

                    val authorMenu = popupMenu.menu.findItem(R.id.action_author_menu)
                    authorMenu.title = post.author.username

                    val deleteMenuItem = popupMenu.menu.findItem(R.id.action_delete_post)
                    deleteMenuItem.isVisible = post.author.id == currentUserId

                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.action_report_post -> {
                                Toast.makeText(
                                    itemView.context,
                                    "Post reported",
                                    Toast.LENGTH_SHORT
                                ).show()
                                true
                            }

                            R.id.action_report_illegal -> {
                                Toast.makeText(
                                    itemView.context,
                                    "Reported as EU illegal content",
                                    Toast.LENGTH_SHORT
                                ).show()
                                true
                            }

                            R.id.action_follow_author -> {
                                Toast.makeText(
                                    itemView.context,
                                    "Followed ${post.author.username}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                true
                            }

                            R.id.action_mute_author -> {
                                Toast.makeText(
                                    itemView.context,
                                    "Muted ${post.author.username}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                true
                            }

                            R.id.action_block_author -> {
                                Toast.makeText(
                                    itemView.context,
                                    "Blocked ${post.author.username}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                true
                            }

                            R.id.action_delete_post -> {
                                // Afficher le dialog de confirmation
                                showDeleteConfirmationDialog(post.id, itemView.context)
                                true
                            }

                            else -> false
                        }
                    }

                    popupMenu.show()
                }

                popupMenu.show()
            }

            btnLike.setOnClickListener {
                onLikeClicked(post.id)
                val newIsLiked = !isLiked
                updateLikeButtonIcon(newIsLiked)
            }

            btnShare.setOnClickListener {
                onShareClicked(post)
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, PostDetailsActivity::class.java)
                intent.putExtra("POST_ID", post.id)
                itemView.context.startActivity(intent)
            }

            btnComment.setOnClickListener {
                showCommentBottomSheet(post.id)
            }

        }

        private fun updateLikeButtonIcon(isLiked: Boolean) {
            if (isLiked) {
                btnLike.setImageResource(R.drawable.ic_favorite_filled24px)
            } else {
                btnLike.setImageResource(R.drawable.ic_favorite_outlined24px)
            }
        }

        private fun showCommentBottomSheet(postId: Int) {
            // Initialisation du BottomSheetDialog
            val bottomSheetDialog = BottomSheetDialog(itemView.context)
            val view = LayoutInflater.from(itemView.context)
                .inflate(R.layout.layout_bottom_sheet_comment, null)

            val etComment = view.findViewById<EditText>(R.id.etComment)
            val btnSubmitComment = view.findViewById<Button>(R.id.btnSubmitComment)

            btnSubmitComment.setOnClickListener {
                val commentText = etComment.text.toString().trim()
                if (commentText.isNotEmpty()) {
                    submitComment(postId, commentText)
                    bottomSheetDialog.dismiss()
                } else {
                    Toast.makeText(itemView.context, "Comment cannot be empty", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        }

        private fun submitComment(parentId: Int, contentText: String) {
            val viewModel =
                ViewModelProvider(itemView.context as AppCompatActivity)[PostViewModel::class.java]
            val userId = sessionManager.getUserId()

            viewModel.createPost(userId, contentText, parentId)

            viewModel.postSuccess.observe(itemView.context as AppCompatActivity) { success ->
                if (success) {
                    Toast.makeText(
                        itemView.context,
                        "Comment added successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(itemView.context, "Failed to add comment", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        private fun showDeleteConfirmationDialog(postId: Int, context: Context) {
            AlertDialog.Builder(context)
                .setTitle("Delete Post")
                .setMessage("Are you sure you want to delete this post?")
                .setPositiveButton("Yes") { _, _ ->
                    // Appel à la fonction pour supprimer le post
                    deletePost(postId, context)
                }
                .setNegativeButton("No", null)
                .show()
        }

        private fun deletePost(postId: Int, context: Context) {
            CoroutineScope(Dispatchers.IO).launch {
                PostRepository().deletePostById(postId)
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
                seconds < 60 -> "$seconds s"
                minutes < 60 -> "$minutes m"
                hours < 24 -> "$hours h"
                days < 7 -> "$days d"
                days < 30 -> "${days / 7} w"
                days < 365 -> "${days / 30} m"
                else -> "${days / 365} y"
            }
        } catch (e: Exception) {
            "unknown"
        }
    }


}
