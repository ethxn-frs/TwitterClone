package com.etang.twitterclone.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.etang.twitterclone.adapter.ConversationsAdapter
import com.etang.twitterclone.databinding.FragmentConversationsBinding
import com.etang.twitterclone.pages.conversations.ConversationsDetailsActivity
import com.etang.twitterclone.pages.conversations.CreateConversationActivity
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.ConversationViewModel

class ConversationsFragment : Fragment() {

    private var _binding: FragmentConversationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private val viewModel: ConversationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConversationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId()

        val adapter = ConversationsAdapter(
            sessionManager = sessionManager,
            onConversationClicked = { conversation ->
                Toast.makeText(
                    requireContext(),
                    "Ouverture de la conversation avec ${conversation.name}",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(requireContext(), ConversationsDetailsActivity::class.java)
                intent.putExtra("CONVERSATION_ID", conversation.id)
                startActivity(intent)
            }
        )

        binding.recyclerViewConversations.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewConversations.adapter = adapter

        binding.fabNewConversations.setOnClickListener {
            val intent = Intent(requireContext(), CreateConversationActivity::class.java)
            startActivity(intent)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchUserConversations(userId)
        }

        observeViewModel(adapter)
        viewModel.fetchUserConversations(userId)
    }

    private fun observeViewModel(adapter: ConversationsAdapter) {
        viewModel.userConversations.observe(viewLifecycleOwner) { conversations ->
            if (conversations.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
            } else {
                binding.emptyView.visibility = View.GONE
            }
            adapter.submitList(conversations)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        viewModel.conversationDetails.observe(viewLifecycleOwner) { updatedConv ->
            updatedConv?.let { conversation ->
                adapter.updateConversation(conversation)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
