package com.articloud.ui.favoritos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.articloud.model.Favorito
import com.articloud.data.Repository.FavoritosRepository
import kotlinx.coroutines.launch

class FavoritosViewModel(private val repo: FavoritosRepository) : ViewModel() {

    private val _favoritos = MutableLiveData<List<Favorito>>()
    val favoritos: LiveData<List<Favorito>> = _favoritos

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _mensaje = MutableLiveData<String?>()
    val mensaje: LiveData<String?> = _mensaje

    // Cargar favoritos del usuario
    fun cargarFavoritos(idUsuario: Int) {
        _loading.value = true
        viewModelScope.launch {
            repo.getFavoritos(idUsuario).fold(
                onSuccess = { _favoritos.value = it },
                onFailure = { _error.value = it.message }
            )
            _loading.value = false
        }
    }

    // Eliminar favorito
    // Eliminar favorito
    fun eliminar(idUsuario: Int, idObra: Int) {
        viewModelScope.launch {
            repo.eliminar(idUsuario, idObra).fold(
                onSuccess = {
                    _mensaje.value = "Eliminado de favoritos"
                    // Filtra por favorito.obra.id_obra
                    _favoritos.value = _favoritos.value
                        ?.filter { it.obra.idObra != idObra }
                },
                onFailure = { _error.value = it.message }
            )
        }
    }
}