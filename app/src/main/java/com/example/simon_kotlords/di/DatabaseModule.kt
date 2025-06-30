package com.example.simon_kotlords.di

// Importazioni necessarie per Room, Dagger Hilt e contesto Android
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

// Indica che questo modulo sarà installato nel SingletonComponent, cioè avrà ciclo di vita globale
@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    // Fornisce un'istanza singleton del database Room
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext, // Contesto dell'applicazione
            AppDatabase::class.java, // Classe astratta che rappresenta il database
            "simon_kotlords_database" // Nome del file del database
        ).build() // Costruisce l'istanza del database
    }

    // Fornisce un'istanza del DAO per accedere ai punteggi
    @Provides
    fun provideHighScoreDao(appDatabase: AppDatabase): HighScoreDao {
        return appDatabase.highScoreDao() // Ottiene il DAO dal database
    }
}
