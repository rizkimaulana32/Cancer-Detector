package com.dicoding.asclepius.repository

import android.util.Log
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.data.remote.retrofit.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class NewsRepository private constructor(
    private val apiService: ApiService,
    private val dispatcher: CoroutineDispatcher
){
    companion object {
        @Volatile
        private var instance: NewsRepository? = null

        fun getInstance(
            apiService: ApiService,
            dispatcher: CoroutineDispatcher
        ): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService, dispatcher)
            }.also { instance = it }
    }

    fun getArticles(apiKey: String): Flow<Result<List<ArticlesItem>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getArticles(apiKey)
            Log.d("NewsRepository", "(getArticles: ${response.articles}")
            emit(Result.Success(response.articles))
        } catch (e: Exception) {
            emit(Result.Error("Tidak ada koneksi internet. News gagal dimuat."))
            Log.e("NewsRepository", "getArticles: ${e.message.toString()}")
        }
    }.flowOn(dispatcher)
}