package com.example.pokemon.ui.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE name = :name AND password = :password")
    fun login(name: String, password: String):User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User):Long
}