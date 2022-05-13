package com.example.pokemon.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.pokemon.ui.room.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    companion object {
        const val USERPREF = "USER_PREFS"
        private val ID_USER_KEY = intPreferencesKey("ID_USER_KEY")
        val USERNAME_KEY = stringPreferencesKey("USERNAME_KEY")
        private val EMAIL_KEY = stringPreferencesKey("EMAIL_KEY")
        val PASSWORD_KEY = stringPreferencesKey("PASSWORD_KEY")
        val IMAGE_KEY = stringPreferencesKey("IMAGE_KEY")
        const val DEFAULT_ID = -1
        const val DEFAULT_USERNAME = "DEF_USERNAME"
        const val DEFAULT_EMAIL = "DEF_EMAIL"
        const val DEFAULT_PASSWORD = "DEF_PASSWORD"
        const val DEFAULT_IMAGE = "DEF_IMAGE"
        val Context.dataStore by preferencesDataStore(DataStoreManager.USERPREF)
    }

    suspend fun setUser(user: User) {
        context.dataStore.edit { preferences ->
            preferences[ID_USER_KEY] = user.id!!.toInt()
            preferences[USERNAME_KEY] = user.name
            preferences[EMAIL_KEY] = user.email
            preferences[PASSWORD_KEY] = user.password
        }
    }
    fun getUser(): Flow<User> {
        return context.dataStore.data.map { preferences ->
            User(
                preferences[ID_USER_KEY] ?: DEFAULT_ID,
                preferences[USERNAME_KEY] ?: DEFAULT_USERNAME,
                preferences[EMAIL_KEY] ?: DEFAULT_EMAIL,
                preferences[PASSWORD_KEY] ?: DEFAULT_PASSWORD,
                preferences[IMAGE_KEY] ?: DEFAULT_IMAGE
            )
        }
    }
    suspend fun deleteUserFromPref() {
        context.dataStore.edit {
            it.clear()
        }
    }
}