package com.farhan164.post5pmob.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.farhan164.post5pmob.model.MyApplication
import com.farhan164.post5pmob.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Ganti 'application: Application' menjadi 'application: MyApplication'
class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val postDao = (application as MyApplication).database.postDao()

    val allPosts: LiveData<List<Post>> = postDao.getAllPosts()

    fun insert(post: Post) = viewModelScope.launch(Dispatchers.IO) {
        postDao.insertPost(post)
    }

    fun update(post: Post) = viewModelScope.launch(Dispatchers.IO) {
        postDao.updatePost(post)
    }

    fun delete(post: Post) = viewModelScope.launch(Dispatchers.IO) {
        postDao.deletePost(post)
    }
}