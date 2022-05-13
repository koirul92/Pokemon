package com.example.pokemon.ui.favorite

import androidx.lifecycle.*
import com.example.pokemon.ui.room.Favorite
import com.example.pokemon.ui.room.repository.FavoriteRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: FavoriteRepository): ViewModel() {

    private val _allFavorites: MutableLiveData<List<Favorite?>> = MutableLiveData()
    val allFavorites: LiveData<List<Favorite?>> = _allFavorites

    fun getAllFavorites(){
        viewModelScope.launch {
            val allFavorites = repository.getFavorites()
            _allFavorites.postValue(allFavorites!!)
        }
    }

}

class FavoriteViewModelFactory(private val repository: FavoriteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}