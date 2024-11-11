package com.dicoding.asclepius.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [History::class], version = 1)
abstract class HistoryRoomDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var instance: HistoryRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): HistoryRoomDatabase {
            if (instance == null) {
                synchronized(HistoryRoomDatabase::class.java) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        HistoryRoomDatabase::class.java, "history_database")
                        .build()
                }
            }
            return instance as HistoryRoomDatabase
        }
    }
}