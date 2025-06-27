package com.example.simon_kotlords.di
import android.content.Context
import androidx.room.Room
import com.example.simon_kotlords.data.local.dao.HighScoreDao
import com.example.simon_kotlords.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "simon_kotlords_database"
        )
            .build()
    }

    @Provides
    fun provideHighScoreDao(appDatabase: AppDatabase): HighScoreDao {
        return appDatabase.highScoreDao()
    }
}