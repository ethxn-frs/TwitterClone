package com.etang.twitterclone.pages.conversations

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.etang.twitterclone.repositories.ConversationRepository
import com.etang.twitterclone.repositories.UserRepository
import com.etang.twitterclone.session.SessionManager
import com.etang.twitterclone.viewmodel.ConversationViewModel

class ConversationViewModelFactory(
    private val sessionManager: SessionManager,
    private val conversationRepository: ConversationRepository = ConversationRepository(),
    private val userRepository: UserRepository = UserRepository()) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ConversationViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ConversationViewModel(sessionManager, conversationRepository, userRepository) as T
        }
        throw IllegalArgumentException("Erreur")
    }

}