package com.etang.twitterclone.fragments

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.PostsAdapter
import com.etang.twitterclone.data.model.Post
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.PostViewModel
import java.util.Locale

class PostDetailsFragment : Fragment() {

    private lateinit var recyclerViewComments: RecyclerView
    private lateinit var commentsAdapter: PostsAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var post: Post
    private lateinit var btnLike: ImageButton
    private lateinit var tvLikes: TextView
    private var isLiked: Boolean = false
    private val viewModel: PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_post_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        val postId = arguments?.getInt("POST_ID") ?: -1
        if (postId == -1) {
            requireActivity().supportFragmentManager.popBackStack()
            return
        }

        recyclerViewComments = view.findViewById(R.id.recyclerViewComments)
        commentsAdapter = PostsAdapter(
            sessionManager,
            onLikeClicked = { postId -> likeComment(postId) },
            onShareClicked = { comment -> shareComment(comment) },
            onProfileClicked = { userId ->
                val fragment = ProfileFragment().apply {
                    arguments = Bundle().apply {
                        putInt("USER_ID", userId)
                    }
                }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        )
        recyclerViewComments.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewComments.adapter = commentsAdapter

        btnLike = view.findViewById(R.id.btnLike)
        tvLikes = view.findViewById(R.id.tvLikes)

        observePostDetails()
        viewModel.fetchPostById(postId)
    }

    private fun observePostDetails() {
        viewModel.postDetails.observe(viewLifecycleOwner) { postDetails ->
            if (postDetails != null) {
                post = postDetails
                displayPostDetails()
                loadComments()
            } else {
                Toast.makeText(requireContext(), "Failed to load post details.", Toast.LENGTH_SHORT)
                    .show()
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun displayPostDetails() {
        view?.findViewById<TextView>(R.id.tvAuthor)?.text =
            "${post.author.firstName} ${post.author.lastName}"
        view?.findViewById<TextView>(R.id.tvAuthorUsername)?.text = "@${post.author.username}"
        view?.findViewById<TextView>(R.id.tvContent)?.text = post.content

        val (createdHour, createdDate) = formatDateTime(post.createdAt)

        view?.findViewById<TextView>(R.id.tvHCreatedHour)?.text = createdHour
        view?.findViewById<TextView>(R.id.tvHCreatedDate)?.text = createdDate
    }

    private fun loadComments() {
        commentsAdapter.submitList(post.comments)
    }

    private fun likeComment(postId: Int) {
    }

    private fun shareComment(comment: Post) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Check out this tweet!")
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out this tweet by ${comment.author.username}:\n\n${comment.content}\n\nShared via TwitterClone"
            )
        }
        startActivity(Intent.createChooser(shareIntent, "Share post via"))
    }

    private fun formatDateTime(createdAt: String): Pair<String, String> {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            val hourFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(createdAt)
            val hour = hourFormat.format(date)
            val formattedDate = dateFormat.format(date)

            Pair(hour, formattedDate)
        } catch (e: Exception) {
            e.printStackTrace()
            Pair("Unknown", "Unknown")
        }
    }
}
