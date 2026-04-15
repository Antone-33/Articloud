package com.articloud.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.articloud.data.Repository.AuthRepository
import com.articloud.model.LoginResponse
import kotlinx.coroutines.launch


class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    private val _registerResult = MutableLiveData<Result<Unit>>()
    val registerResult: LiveData<Result<Unit>> = _registerResult

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun login(email: String, password: String) {
        _loading.value = true
        viewModelScope.launch {
            _loginResult.value = repo.login(email, password)
            _loading.value = false
        }
    }

    fun register(nombre: String, email: String, password: String,
                 telefono: String, direccion: String) {
        _loading.value = true
        viewModelScope.launch {
            _registerResult.value = repo.register(nombre, email, password, telefono, direccion)
            _loading.value = false
        }
    }
}