package com.example.pokemon.ui.pokeinfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pokemon.model.Pokemon
import com.example.pokemon.service.PokeApiClient
import com.example.pokemon.ui.room.Favorite
import com.example.pokemon.ui.room.UserDatabase
import com.example.pokemon.ui.room.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoViewModel(private val repository: FavoriteRepository):ViewModel() {

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
    //favorite
    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite = _isFavorite

    fun changeFavorite(state: Boolean){
        _isFavorite.postValue(state)
    }

    fun getFavorite(id:Int) = repository.getFavoriteById(id)
    fun addFavorite(favorite: Favorite) = repository.insertFavorite(favorite)
    fun deleteFavorite(favorite: Favorite) = repository.deleteFavorite(favorite)
}

@Suppress("UNCHECKED_CAST")
class InfoViewModelFactory(private val repository: FavoriteRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InfoViewModel::class.java)){
            return InfoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}