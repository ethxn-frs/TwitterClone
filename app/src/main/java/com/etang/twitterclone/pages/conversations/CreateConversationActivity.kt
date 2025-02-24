package com.etang.twitterclone.pages.conversations

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.MultiAutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.etang.twitterclone.R
import com.etang.twitterclone.data.model.User
import com.etang.twitterclone.repositories.UserRepository
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.ConversationViewModel
import com.etang.twitterclone.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class CreateConversationActivity : AppCompatActivity() {

    private val conversationViewModel: ConversationViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val userRepository = UserRepository()

    private lateinit var sessionManager: SessionManager

    private lateinit var allParticipants: MultiAutoCompleteTextView
    private lateinit var btnCreateConversation: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_conversation)

        sessionManager = SessionManager(this)
        val creatorId = sessionManager.getUserId()
        allParticipants = findViewById(R.id.allParticipants)
        btnCreateConversation = findViewById(R.id.btnCreateConversation)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }
        observeViewModel()

        loadFollowersSuggestions(creatorId)
        allParticipants.threshold = 0
        allParticipants.setOnClickListener {
            allParticipants.showDropDown()
        }
        allParticipants.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        btnCreateConversation.setOnClickListener {
            val participantsText = allParticipants.text.toString().trim()
            if(participantsText.isNotEmpty()){
                val usernames = participantsText.split(",").map {it.trim()}.filter { it.isNotEmpty() }
                conversationViewModel.createConversation(creatorId, usernames)
            }else{
                Toast.makeText(this, "Veuillez saisir au moins un username", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadFollowersSuggestions(currentUserId: Int){
        lifecycleScope.launch {
            try {
                val followers = userViewModel.getUserFollowing(currentUserId) ?: emptyList()
                val allUsers = userViewModel.getAllUsersInDatabase() ?: emptyList()

                val followerUsernames = followers.map { it.username }
                val suggestionsUsernames = allUsers.filter { it.username !in followerUsernames }
                val limitedSuggestions = mutableListOf<String>()
                for(user in suggestionsUsernames.take(5)){
                    val follows = conversationViewModel.isUserFollowingCreator(currentUserId, user.id)
                    if(!follows){
                        limitedSuggestions.add(user.username)
                    }else{
                        limitedSuggestions.add("${user.username} (il vous suit)")
                    }
                }

                val items = mutableListOf<String>()

                val headerFollowers = if (followerUsernames.size > 1)
                    "Liste des utilisateurs que vous suivez"
                else if (followerUsernames.size == 1)
                    "Vous suivez un utilisateur"
                else
                    "Vous ne suivez personne"
                items.add(headerFollowers)
                items.add("------------")
                items.addAll(followerUsernames)
                items.add("------------")
                val headerAllUsernames = "Liste des suggestions"
                items.add(headerAllUsernames)
                items.add("------------")
                items.addAll(limitedSuggestions)
                val adapter = object : ArrayAdapter<String>(
                    this@CreateConversationActivity,
                    android.R.layout.simple_dropdown_item_1line,
                    items
                ){
                    override fun isEnabled(position: Int): Boolean{
                        return when (position){
                            0, followerUsernames.size + 1 -> false
                            else -> true
                        }
                    }
                    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View{
                        val view = super.getDropDownView(position, convertView, parent)
                        when(position){
                            0, followerUsernames.size + 1 -> (view as TextView).setTextColor(Color.GRAY)
                            else -> (view as TextView).setTextColor(Color.BLACK)
                        }
                        return view
                    }
                }
                allParticipants.setAdapter(adapter)
            }catch(e:Exception){
                e.printStackTrace()
                Toast.makeText(this@CreateConversationActivity, "Erreur lors du chargement des utilisateurs", Toast.LENGTH_SHORT).show()
            }



        }
    }

    private fun observeViewModel() {
        conversationViewModel.error.observe(this) { errorMessage ->
            errorMessage?.let{
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        conversationViewModel.creationSuccess.observe(this) { success ->
            if(success){
                Toast.makeText(this, "Creation en cours", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }

}