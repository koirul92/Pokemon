package com.example.pokemon.ui.pokelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.pokemon.datastore.DataStoreManager
import com.example.pokemon.model.PokeApiResponse
import com.example.pokemon.model.PokeResult
import com.example.pokemon.service.PokeApiClient
import com.example.pokemon.ui.room.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListViewModel(private val pref: DataStoreManager):ViewModel() {
    val pokemonList = MutableLiveData<List<PokeResult>>()
    private lateinit var viewModel: ListViewModel
    val id = pokemonList
    fun getPokemonList(){
        val call = PokeApiClient.instance.getPokemonList(1000,0)
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
    suspend fun setDataUser(user: User) {
        pref.setUser(user)
    }

    fun getDataUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }

}