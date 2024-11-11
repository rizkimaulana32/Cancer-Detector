package com.dicoding.asclepius.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.repository.HistoryRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyRepository: HistoryRepository) : ViewModel() {
    val historyList: LiveData<List<History>> = historyRepository.getAllHistory()

    fun delete(history: History) {
        viewModelScope.launch {
            historyRepository.delete(history)
        }
    }
}
