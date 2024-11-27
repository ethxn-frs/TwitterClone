package com.etang.twitterclone.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etang.twitterclone.model.Post
import com.etang.twitterclone.repository.PostRepository
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    private val repository = PostRepository()

    private val _postSuccess = MutableLiveData<Boolean>()
    val postSuccess: LiveData<Boolean> get() = _postSuccess

    fun createPost(userId: Int, content: String, parentId: Int? = null) {
        viewModelScope.launch {
            val result = repository.createPost(content, userId, parentId)
            _postSuccess.value = result
        }
    }

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> get() = _posts

    fun fetchPosts() {
        viewModelScope.launch {
            val fetchedPosts = repository.getPosts()
            _posts.value = fetchedPosts
        }
    }


}