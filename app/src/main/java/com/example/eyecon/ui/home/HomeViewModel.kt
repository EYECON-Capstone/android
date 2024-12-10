package com.example.eyecon.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eyecon.data.news.api.RetrofitClient
import com.example.eyecon.data.news.dataclass.Article
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _profilePhotoUrl = MutableLiveData<String>()
    val profilePhotoUrl: LiveData<String> = _profilePhotoUrl

    private val _newsArticles = MutableLiveData<List<Article>>()
    val newsArticles: LiveData<List<Article>> = _newsArticles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val apiKey = "34fcaf6fe9ff47f9831dd7297461d892"

    fun loadNews() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val response = RetrofitClient.newsApiService.getNews(
                    query = "mata kesehatan",
                    apiKey = apiKey
                )

                _newsArticles.value = response.articles
                _isLoading.postValue(false)
            } catch (e: Exception) {
                _error.value = "Failed to load news: ${e.localizedMessage}"
                _isLoading.value = false
            }
        }
    }
    fun showusername() {
        val user = FirebaseAuth.getInstance().currentUser
        _username.value = user?.displayName
    }

    fun showphoto() {
        val user = FirebaseAuth.getInstance().currentUser
        _profilePhotoUrl.value = user?.photoUrl?.toString()
    }

}