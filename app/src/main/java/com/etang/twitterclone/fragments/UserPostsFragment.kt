package com.etang.twitterclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.etang.twitterclone.adapter.PostsAdapter
import com.etang.twitterclone.data.dto.PostDto
import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.databinding.FragmentUserPostsBinding
import com.etang.twitterclone.session.SessionManager

class UserPostsFragment : Fragment() {

    private var _binding: FragmentUserPostsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PostsAdapter
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        adapter = PostsAdapter(sessionManager, {}, {})

        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPosts.adapter = adapter

        // Posts en dur pour exemple
        val dummyUser = User(1, "John", "Doe", "johndoe", "", "", "", "", "")
        adapter.submitList(
            listOf(
                PostDto(1, "Hello TwitterClone!", "2024-02-25", dummyUser.username, 12, 3),
                PostDto(2, "Second post", "2024-02-24", dummyUser.username, 4, 1),
            ).map { dto ->
                dto.toPost(dummyUser)
            }
        )
    }

    private fun PostDto.toPost(user: User) = com.etang.twitterclone.data.model.Post(
        id = id,
        content = content,
        createdAt = createdAt,
        author = user,
        comments = List(commentsCount) { dummyComment(it, user) },
        userHaveLiked = List(likesCount) { user },
        deleted = false
    )

    private fun dummyComment(id: Int, user: User) = com.etang.twitterclone.data.model.Post(
        id = id,
        content = "Comment $id",
        createdAt = "2024-02-20",
        author = user
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
