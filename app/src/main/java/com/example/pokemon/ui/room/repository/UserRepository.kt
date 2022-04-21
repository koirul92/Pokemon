package com.example.pokemon.ui.room.repository

import android.content.Context
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.pokemon.ui.room.User
import com.example.pokemon.ui.room.UserDatabase

class UserRepository (private val context: Context){
    private val myDatabase = UserDatabase.getInstance(context)
    suspend fun login(email: String, password: String): User?{
        return myDatabase?.userDao()?.login(email, password)
    }
    suspend fun getUser(name: String, password: String):User?{
        return myDatabase?.userDao()?.getUser(name,password)
    }

    suspend fun insertUser(user: User):Long?{
        return myDatabase?.userDao()?.insertUser(user)
    }

    suspend fun updateItem(user: User):Int?{
        return myDatabase?.userDao()?.updateItem(user)
    }

}