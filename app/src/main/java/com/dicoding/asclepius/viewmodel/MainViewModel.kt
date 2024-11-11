package com.dicoding.asclepius.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.repository.NewsRepository
import kotlinx.coroutines.launch
import com.dicoding.asclepius.repository.Result

class MainViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val _listArticles = MutableLiveData<List<ArticlesItem>>()
    val listArticles: LiveData<List<ArticlesItem>> = _listArticles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri: LiveData<Uri?> = _currentImageUri

    init {
        getArticles()
    }

    fun setImageUri(uri: Uri?) {
        _currentImageUri.value = uri
    }

    private fun getArticles(apiKey: String = BuildConfig.API_KEY) = viewModelScope.launch {
        newsRepository.getArticles(apiKey).collect { articles ->
            when (articles) {
                is Result.Loading -> {
                    _isLoading.value = true
                }
                is Result.Success -> {
                    _isLoading.value = false
                    _listArticles.value = articles.data
                    _error.value = null
                }
                is Result.Error -> {
                    _isLoading.value = false
                    _error.value = articles.error
                }
            }
        }
    }
}