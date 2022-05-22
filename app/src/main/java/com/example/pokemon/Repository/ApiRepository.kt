package com.example.pokemon.Repository

import com.example.pokemon.service.ApiHelper

class ApiRepository(private val apiHelper: ApiHelper) {
    suspend fun getPokemonInfo(id:Int) = apiHelper.getPokemonInfo(id)
    suspend fun getPokemonList(limit:Int,offset:Int) = apiHelper.getPokemonList(limit, offset)
}