package com.example.pokemon.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemon.Repository.DetailRepository
import com.example.pokemon.Repository.FavoriteRepository
import com.example.pokemon.local.Favorite
import com.example.pokemon.model.PokeApiResponse
import com.example.pokemon.model.Pokemon
import com.example.pokemon.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(private val repository: DetailRepository): ViewModel() {

    private val _pokemonInfo : MutableLiveData<Resource<Pokemon>> = MutableLiveData()
    val pokemonInfo: LiveData<Resource<Pokemon>> get() = _pokemonInfo

    fun getPokemonInfo(id:Int){
        viewModelScope.launch {
            _pokemonInfo.postValue(Resource.loading())
            try {
                _pokemonInfo.postValue(Resource.success(repository.getPokemonInfo(id)))
            }catch (exception:Exception){
                _pokemonInfo.postValue(Resource.error(exception.localizedMessage?:"Error Occurred"))
            }
        }
    }
//favorite
    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite = _isFavorite

    fun changeFavorite(state: Boolean){
        _isFavorite.postValue(state)
    }
    fun getFavorite(id:Int) = repository.getFavoritebyId(id)
    fun addFavorite(favorite: Favorite) = repository.insertFavorite(favorite)
    fun deleteFavorite(favorite: Favorite) = repository.deleteFavorite(favorite)
}