package com.example.pokemon.Repository

import com.example.pokemon.local.Favorite
import com.example.pokemon.local.FavoriteDao
import com.example.pokemon.service.PokeApiService

class DetailRepository(private val apiRepository: ApiRepository, private val favoriteDao: FavoriteDao) {
    suspend fun getPokemonInfo(id:Int) = apiRepository.getPokemonInfo(id)
    fun getFavoritebyId(id: Int) = favoriteDao.getFavoriteById(id)
    fun insertFavorite(favorite: Favorite) = favoriteDao.insertFavorite(favorite)
    fun deleteFavorite(favorite: Favorite) = favoriteDao.deleteFavorite(favorite)
}