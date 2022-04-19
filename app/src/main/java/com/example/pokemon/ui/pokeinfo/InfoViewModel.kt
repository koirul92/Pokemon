package com.example.pokemon.ui.pokeinfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokemon.model.Pokemon
import com.example.pokemon.service.PokeApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoViewModel():ViewModel() {

    val pokemonInfo = MutableLiveData<Pokemon>()

    fun getPokemonInfo(id: Int){
        val call = PokeApiClient.instance.getPokemonInfo(id)

        call.enqueue(object : Callback<Pokemon>{
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                response.body()?.let { pokemon ->
                    pokemonInfo.postValue(pokemon)
                }
            }
            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                call.cancel()
            }
        })
    }
}