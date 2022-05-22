package com.example.pokemon.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        User::class,Favorite::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun userDao():UserDao
    abstract fun favoriteDao():FavoriteDao
}