package com.etang.twitterclone.pages

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etang.twitterclone.R
import com.etang.twitterclone.adapter.PostsAdapter
import com.etang.twitterclone.adapter.UsersAdapter
import com.etang.twitterclone.repositories.PostRepository
import com.etang.twitterclone.repositories.UserRepository
import com.etang.twitterclone.session.SessionManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var viewFlipper: ViewFlipper
    private lateinit var recyclerViewPosts: RecyclerView
    private lateinit var recyclerViewUsers: RecyclerView
    private lateinit var postsAdapter: PostsAdapter
    private lateinit var usersAdapter: UsersAdapter

    private val postRepository = PostRepository()
    private val userRepository = UserRepository()

    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        etSearch = findViewById(R.id.etSearch)
        progressBar = findViewById(R.id.progressBar)
        viewFlipper = findViewById(R.id.viewFlipper)

        recyclerViewPosts = findViewById(R.id.recyclerViewPreviewPosts)
        recyclerViewUsers = findViewById(R.id.recyclerViewPreviewUsers)

        recyclerViewPosts.layoutManager = LinearLayoutManager(this)
        recyclerViewUsers.layoutManager = LinearLayoutManager(this)

        postsAdapter = PostsAdapter(
            sessionManager = SessionManager(this),
            onLikeClicked = { /* Like action */ },
            onShareClicked = { /* Share action */ }
        )

        usersAdapter = UsersAdapter { userId -> /* Open user profile */ }

        recyclerViewPosts.adapter = postsAdapter
        recyclerViewUsers.adapter = usersAdapter

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchJob?.cancel()
                if (s.toString().length >= 2) {
                    progressBar.visibility = View.VISIBLE
                    searchJob = lifecycleScope.launch {
                        delay(300) // Ajout d'un délai pour éviter les requêtes inutiles
                        fetchPreviewResults(s.toString())
                    }
                } else {
                    clearResults()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun fetchPreviewResults(query: String) {
        lifecycleScope.launch {
            val posts = postRepository.searchPosts(query)
            val users = userRepository.searchUsers(query)

            progressBar.visibility = View.GONE

            if (posts.isNotEmpty() || users.isNotEmpty()) {
                viewFlipper.displayedChild = 1 // Afficher les résultats
                postsAdapter.submitList(posts)
                usersAdapter.submitList(users)
            } else {
                viewFlipper.displayedChild = 2 // Afficher un message "Aucun résultat"
            }
        }
    }

    private fun clearResults() {
        postsAdapter.submitList(emptyList())
        usersAdapter.submitList(emptyList())
        viewFlipper.displayedChild = 0 // Afficher la page d'accueil
        progressBar.visibility = View.GONE
    }
}
