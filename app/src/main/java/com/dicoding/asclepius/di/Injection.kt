package com.dicoding.asclepius.di

import android.app.Application
import com.dicoding.asclepius.data.local.HistoryRoomDatabase
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig
import com.dicoding.asclepius.repository.HistoryRepository
import com.dicoding.asclepius.repository.NewsRepository
import kotlinx.coroutines.Dispatchers

object Injection {
    fun provideHistoryRepository(application: Application): HistoryRepository {
        val database = HistoryRoomDatabase.getDatabase(application)
        val dao = database.historyDao()
        val dispatcher = Dispatchers.IO
        return HistoryRepository.getInstance(dao, dispatcher)
    }

    fun provideNewsRepository(): NewsRepository {
        val apiService = ApiConfig.getApiService()
        val dispatcher = Dispatchers.IO
        return NewsRepository.getInstance(apiService, dispatcher)
    }
}