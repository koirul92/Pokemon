package com.example.pokemon.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE name = :name AND password = :password")
    suspend fun login(name: String, password: String):User

    @Query("SELECT * FROM User WHERE name = :name")
    suspend fun getUser(name: String):User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User):Int
}