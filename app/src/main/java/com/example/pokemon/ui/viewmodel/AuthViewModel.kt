package com.example.pokemon.ui.viewmodel

import androidx.lifecycle.*
import com.example.pokemon.Repository.UserRepository
import com.example.pokemon.datastore.PreferenceModel
import com.example.pokemon.local.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: UserRepository):ViewModel() {
    private val _user: MutableLiveData<User?> = MutableLiveData()
    val user: LiveData<User?> = _user

    private val _userSession: MutableLiveData<PreferenceModel> = MutableLiveData()
    val userSession: LiveData<PreferenceModel> get() = _userSession

    private val _uiState:MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val uiState: LiveData<LoginUiState> get() = _uiState.asLiveData()

    fun login(
        name: String,
        password: String,
    ) {
        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            try {
                val loginResult = repository.login(
                    name = name,
                    password = password,
                )
                loginResult?.collectLatest { user ->
                    repository.setUser(
                        PreferenceModel(
                            name = user.name,
                            email = user.email,
                        )
                    )
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoggedIn = true,
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "User Not Found",
                    )
                }
            }
        }
    }


    fun getUser(name: String){
        viewModelScope.launch {
            val newUser = repository.getUserRoom(name)
            _user.postValue(newUser)
        }
    }
    fun updateUser(user: User) = repository.updateUser(user)

    fun register(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.register(user)
        }
    }

    fun setUser(user: PreferenceModel){
        viewModelScope.launch{
            repository.setUser(user)
        }
    }
    fun getUserfromPref(user: PreferenceModel){
        viewModelScope.launch {
            repository.getUserPref().collect {
                _userSession.value = it
            }
        }
    }
}


data class LoginUiState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)