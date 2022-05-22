package com.example.pokemon.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE name = :name AND password = :password")
    fun login(name: String, password: String):Flow<User>?

    @Query("SELECT * FROM User WHERE name = :name")
    fun getUser(name: String):User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User):Int
}