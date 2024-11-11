package com.dicoding.asclepius.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(history: History)

    @Delete
    suspend fun delete(history: History)

    @Query("SELECT * FROM history ORDER BY id ASC")
    fun getAllHistory(): LiveData<List<History>>
}