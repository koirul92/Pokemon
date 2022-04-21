package com.example.pokemon.ui.room

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE name = :name AND password = :password")
    fun login(name: String, password: String):User?

    @Query("SELECT * FROM User WHERE name = :name AND password = :password")
    fun getUser(name: String, password: String):User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User):Long

    @Update
    fun updateItem(user: User):Int
}