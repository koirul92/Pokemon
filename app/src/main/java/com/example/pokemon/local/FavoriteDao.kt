package com.example.pokemon.local

import androidx.room.*

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM Favorite")
    suspend fun getFavorites(): List<Favorite>

    @Query("SELECT * FROM Favorite WHERE id=:id")
    fun getFavoriteById(id: Int): Favorite?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favorite: Favorite):Long

    @Delete
    fun deleteFavorite(favorite: Favorite):Int
}