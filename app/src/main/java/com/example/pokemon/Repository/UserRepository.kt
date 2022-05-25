package com.example.pokemon.Repository

import com.example.pokemon.datastore.DataStoreManager
import com.example.pokemon.local.User
import com.example.pokemon.local.UserDao
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao, private val pref:DataStoreManager) {
    suspend fun login(name: String, password: String):User = userDao.login(name, password)
    suspend fun register(user: User):Long = userDao.insertUser(user)
    suspend fun updateUser(user: User):Int = userDao.updateUser(user)
    suspend fun setUser(user: User) = pref.setUser(user)
    fun getUserPref(): Flow<User> = pref.getUser()
}