package com.dicoding.asclepius.repository

import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.data.local.HistoryDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class HistoryRepository private constructor(
    private val historyDao: HistoryDao,
    private val dispatcher: CoroutineDispatcher
) {
    companion object {
        @Volatile
        private var instance: HistoryRepository? = null

        fun getInstance(
            dao: HistoryDao,
            dispatcher: CoroutineDispatcher
        ): HistoryRepository =
            instance ?: synchronized(this) {
                instance ?: HistoryRepository(dao, dispatcher)
            }.also { instance = it }
    }

    fun getAllHistory() = historyDao.getAllHistory()

    suspend fun insert(history: History) = withContext(dispatcher) {
        historyDao.insert(history)
    }

    suspend fun delete(history: History) = withContext(dispatcher) {
        historyDao.delete(history)
    }
}