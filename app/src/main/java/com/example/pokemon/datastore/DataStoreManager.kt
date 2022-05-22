package com.example.pokemon.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val NAME_KEY = stringPreferencesKey("name")
private val EMAIL_KEY = stringPreferencesKey("email")

class DataStoreManager(private val dataStore: DataStore<Preferences>) {

    fun getUserSession(): Flow<PreferenceModel> = dataStore.data.map { pref ->
        PreferenceModel(
            name = pref[NAME_KEY] ?: "",
            email = pref[EMAIL_KEY] ?: "",
        )
    }

    suspend fun saveUserSession(user: PreferenceModel) {
        dataStore.edit { pref ->
            pref[NAME_KEY] = user.name
            pref[EMAIL_KEY] = user.email
        }
    }

    suspend fun deleteUserSession() {
        dataStore.edit { pref ->
            pref[NAME_KEY] = ""
            pref[EMAIL_KEY] = ""
        }
    }
}