package com.example.pokemon.di


import android.content.Context
import androidx.room.Room
import com.example.pokemon.local.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): PokemonDatabase = Room.databaseBuilder(appContext,PokemonDatabase::class.java,"pokemon_db").fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun providesUserDao(database: PokemonDatabase) = database.userDao()

    @Singleton
    @Provides
    fun providesFavoriteDao(database: PokemonDatabase) = database.favoriteDao()
}