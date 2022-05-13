package com.example.pokemon.ui.pokelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pokemon.datastore.DataStoreManager
import java.lang.IllegalArgumentException

class ViewModelFactory(private val pref: DataStoreManager) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)){
            return ListViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: "+modelClass.name)
    }
}