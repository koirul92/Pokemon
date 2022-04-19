package com.example.pokemon.ui.pokelist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokemon.model.PokeApiResponse
import com.example.pokemon.model.PokeResult
import com.example.pokemon.service.PokeApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListViewModel():ViewModel() {
    val pokemonList = MutableLiveData<List<PokeResult>>()
    fun getPokemonList(){
        val call = PokeApiClient.instance.getPokemonList(100,0)
        call.enqueue(object : Callback<PokeApiResponse>{
            override fun onResponse(call: Call<PokeApiResponse>,response: Response<PokeApiResponse>) {
                response.body()?.results?.let { list ->
                    pokemonList.postValue(list)
                }
            }
            override fun onFailure(call: Call<PokeApiResponse>, t: Throwable) {
                call.cancel()
            }
        })
    }
}