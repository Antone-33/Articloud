package com.articloud.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.articloud.data.Repository.ObrasRepository
import com.articloud.model.Obra
import kotlinx.coroutines.launch

class DetailViewModel(private val repo: ObrasRepository) : ViewModel() {

    private val _obra = MutableLiveData<Obra>()
    val obra: LiveData<Obra> = _obra

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun getObrasById(id: Int) {
        _loading.value = true

        viewModelScope.launch {
            repo.getObrasById(id).fold(
                onSuccess = { _obra.value = it },
                onFailure = { _error.value = it.message }
            )
            _loading.value = false
        }
    }
}