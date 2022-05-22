package com.example.pokemon.Repository

import com.example.pokemon.local.FavoriteDao

class FavoriteRepository(private val favoriteDao: FavoriteDao) {
    suspend fun getAllFavorite() = favoriteDao.getFavorites()
}