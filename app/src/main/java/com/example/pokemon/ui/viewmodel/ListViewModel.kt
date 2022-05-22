package com.example.pokemon.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemon.Repository.ListRepository
import com.example.pokemon.Repository.UserRepository
import com.example.pokemon.datastore.DataStoreManager
import com.example.pokemon.datastore.PreferenceModel
import com.example.pokemon.model.PokeApiResponse
import com.example.pokemon.model.PokeResult
import com.example.pokemon.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val listRepository: ListRepository): ViewModel() {
    private val _pokemonList : MutableLiveData<Resource<PokeApiResponse>> = MutableLiveData()
    val pokemonList: LiveData<Resource<PokeApiResponse>> get() = _pokemonList
    fun getPokemonList(){
        viewModelScope.launch {
            _pokemonList.postValue(Resource.loading())
            try {
                _pokemonList.postValue(Resource.success(listRepository.getListPokemon(1000,0)))
            }catch (exception:Exception){
                _pokemonList.postValue(Resource.error(exception.localizedMessage?:"Error Occurred"))
            }
        }
    }

    private val _userSession: MutableLiveData<PreferenceModel> = MutableLiveData()
    val userSession: LiveData<PreferenceModel> get() = _userSession


    fun getDataUser() {
        viewModelScope.launch {
            listRepository.getUserPref().collect {
                _userSession.value = it
            }
        }
    }

    fun deleteDataUser(){
        viewModelScope.launch {
            listRepository.deletePref()
        }
    }

}