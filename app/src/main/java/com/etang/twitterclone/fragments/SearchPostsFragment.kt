package com.etang.twitterclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.PostsAdapter
import com.etang.twitterclone.repositories.PostRepository
import com.etang.twitterclone.session.SessionManager
import kotlinx.coroutines.launch

class SearchPostsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostsAdapter
    private val postRepository = PostRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewSearchPosts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = PostsAdapter(
            sessionManager = SessionManager(requireContext()),
            onLikeClicked = { /* Like action */ },
            onShareClicked = { /* Share action */ },
        )

        recyclerView.adapter = adapter

        val searchInput = requireActivity().findViewById<EditText>(R.id.etSearch)
        searchInput.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                searchPosts(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun searchPosts(query: String) {
        if (query.length < 2) return

        lifecycleScope.launch {
            val result = postRepository.searchPosts(query)
            adapter.submitList(result)
        }
    }
}
