package com.example.simon_kotlords.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.simon_kotlords.data.local.dao.HighScoreDao
import com.example.simon_kotlords.data.model.HighScoreEntity

@Database(entities = [HighScoreEntity::class], version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun highScoreDao(): HighScoreDao
}