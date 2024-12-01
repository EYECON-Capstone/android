package com.example.eyecon.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eyecon.data.api.RetrofitClient
import com.example.eyecon.data.dataclass.Article
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val _newsState = MutableLiveData<List<Article>>()
    val newsState: LiveData<List<Article>> = _newsState

    private val _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> = _errorState

    fun fetchNews() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.newsApiService.getNews(
                    apiKey = "34fcaf6fe9ff47f9831dd7297461d892"
                )
                _newsState.value = response.articles
            } catch (e: Exception) {
                _errorState.value = e.message
            }
        }
    }
}