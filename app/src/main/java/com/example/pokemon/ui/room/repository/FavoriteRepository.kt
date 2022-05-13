package com.example.pokemon.ui.room.repository

import android.content.Context
import com.example.pokemon.ui.room.Favorite
import com.example.pokemon.ui.room.User
import com.example.pokemon.ui.room.UserDatabase

class FavoriteRepository(val context: Context) {
    private val myDatabase = UserDatabase.getInstance(context)
    suspend fun getFavorites(): List<Favorite>?{
        return myDatabase?.favoriteDao()?.getFavorites()
    }
    fun getFavoriteById(id: Int): Favorite?{
        return myDatabase?.favoriteDao()?.getFavoriteById(id)
    }

    fun insertFavorite(favorite: Favorite):Long?{
        return myDatabase?.favoriteDao()?.insertFavorite(favorite)
    }

    fun deleteFavorite(favorite: Favorite):Int?{
        return myDatabase?.favoriteDao()?.deleteFavorite(favorite)
    }
}