package com.example.pokemon.Repository

import androidx.lifecycle.asLiveData
import com.example.pokemon.datastore.DataStoreManager
import com.example.pokemon.datastore.PreferenceModel
import com.example.pokemon.local.User
import com.example.pokemon.local.UserDao
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao, private val pref:DataStoreManager) {
    fun login(name: String, password: String):Flow<User>? = userDao.login(name, password)
    fun register(user: User) = userDao.insertUser(user)
    fun getUserRoom(name: String): User? = userDao.getUser(name)
    fun updateUser(user: User):Int = userDao.updateUser(user)
    suspend fun setUser(user: PreferenceModel) = pref.saveUserSession(user)
    fun getUserPref(): Flow<PreferenceModel> = pref.getUserSession()
}