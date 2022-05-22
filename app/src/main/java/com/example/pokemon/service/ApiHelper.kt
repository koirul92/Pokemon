package com.example.pokemon.service

import javax.inject.Inject

class ApiHelper @Inject constructor(private val apiService: PokeApiService) {
    suspend fun getPokemonInfo(id:Int) = apiService.getPokemonInfo(id)
    suspend fun getPokemonList( limit:Int, offset:Int) = apiService.getPokemonList(limit,offset)
}