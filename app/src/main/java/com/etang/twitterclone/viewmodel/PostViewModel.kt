package com.etang.twitterclone.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etang.twitterclone.data.model.Post
import com.etang.twitterclone.repositories.PostRepository
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    private val repository = PostRepository()

    private val _postSuccess = MutableLiveData<Boolean>()
    val postSuccess: LiveData<Boolean> get() = _postSuccess

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> get() = _posts

    private val _likeSuccess = MutableLiveData<Boolean>()
    val likeSuccess: LiveData<Boolean> get() = _likeSuccess

    private val _postDetails = MutableLiveData<Post?>()
    val postDetails: LiveData<Post?> get() = _postDetails

    fun fetchPostById(postId: Int) {
        viewModelScope.launch {
            val post = repository.getPostById(postId)
            _postDetails.postValue(post)
        }
    }

    fun createPost(userId: Int, content: String, parentId: Int? = null) {
        viewModelScope.launch {
            val result = repository.createPost(content, userId, parentId)
            _postSuccess.value = result
        }
    }

    fun fetchPosts() {
        viewModelScope.launch {
            val fetchedPosts = repository.getPosts()
            _posts.value = fetchedPosts
        }
    }

    fun fetchFollowingPosts(userId: Int) {
        viewModelScope.launch {
            val fetchedPosts = repository.getFollowingPosts(userId)
            _posts.value = fetchedPosts
        }
    }

    fun likePost(postId: Int, userId: Int) {
        viewModelScope.launch {
            val result = repository.likePost(postId, userId)
            _likeSuccess.value = result
        }
    }
}