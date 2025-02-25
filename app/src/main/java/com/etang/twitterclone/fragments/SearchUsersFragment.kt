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
import com.etang.twitterclone.adapter.UsersAdapter
import com.etang.twitterclone.repositories.UserRepository
import kotlinx.coroutines.launch

class SearchUsersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UsersAdapter
    private val userRepository = UserRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewSearchUsers)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = UsersAdapter { userId ->
            // Ouvrir le profil de l'utilisateur sélectionné
        }

        recyclerView.adapter = adapter

        val searchInput = requireActivity().findViewById<EditText>(R.id.etSearch)
        searchInput.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                searchUsers(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun searchUsers(query: String) {
        if (query.length < 2) return

        lifecycleScope.launch {
            val result = userRepository.searchUsers(query)
            adapter.submitList(result)
        }
    }
}
