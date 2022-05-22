package com.example.pokemon.Repository

import com.example.pokemon.datastore.DataStoreManager
import com.example.pokemon.datastore.PreferenceModel
import com.example.pokemon.service.ApiHelper
import kotlinx.coroutines.flow.Flow

class ListRepository(private val apiRepository: ApiRepository, private val pref: DataStoreManager) {
    suspend fun getListPokemon(limit:Int,offset:Int) = apiRepository.getPokemonList(limit, offset)
    fun getUserPref(): Flow<PreferenceModel> = pref.getUserSession()
    suspend fun deletePref() = pref.deleteUserSession()
}