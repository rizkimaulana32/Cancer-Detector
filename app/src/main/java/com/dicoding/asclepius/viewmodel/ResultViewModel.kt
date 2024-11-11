package com.dicoding.asclepius.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.repository.HistoryRepository
import kotlinx.coroutines.launch

class ResultViewModel(private val historyRepository: HistoryRepository) : ViewModel() {
    fun insert(history: History) {
        viewModelScope.launch {
            historyRepository.insert(history)
        }
    }
}