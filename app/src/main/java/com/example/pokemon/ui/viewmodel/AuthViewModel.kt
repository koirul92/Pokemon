package com.example.pokemon.ui.viewmodel

import androidx.lifecycle.*
import com.example.pokemon.Repository.UserRepository
import com.example.pokemon.local.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: UserRepository):ViewModel() {

    private val _userSession: MutableLiveData<User> = MutableLiveData()
    val userSession: LiveData<User> get() = _userSession

    fun login(name: String,password:String){

        viewModelScope.launch {
            repository.login(name,password)
        }
    }
    fun setDataUser(user: User){
        viewModelScope.launch {
            repository.setUser(user)
        }
    }
    fun getUser(name: String){
        viewModelScope.launch {
            val newUser = repository.getUserRoom(name)
            _userSession.postValue(newUser)
        }
    }
    fun updateUser(user: User){
        viewModelScope.launch {
            repository.updateUser(user)
        }
    }

    fun register(user: User) {
        viewModelScope.launch {
            repository.register(user)
        }
    }

    fun getUserFromPref(){
        viewModelScope.launch {
            repository.getUserPref().collect {
                _userSession.value = it
            }
        }
    }
}
