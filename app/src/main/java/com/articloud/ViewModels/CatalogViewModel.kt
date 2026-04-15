package com.articloud.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.articloud.data.Repository.ObrasRepository
import com.articloud.model.Categoria
import com.articloud.model.Obra
import kotlinx.coroutines.launch

class CatalogViewModel(private val repo: ObrasRepository) : ViewModel() {

    private val _obras = MutableLiveData<List<Obra>>()
    val obras: LiveData<List<Obra>> = _obras

    private val _categorias = MutableLiveData<List<Categoria>>()
    val categorias: LiveData<List<Categoria>> = _categorias

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _favoritosIds = MutableLiveData<Set<Int>>(emptySet())
    val favoritosIds: LiveData<Set<Int>> = _favoritosIds

    fun loadObras() {
        _loading.value = true
        viewModelScope.launch {
            repo.getObras().fold(
                onSuccess = { _obras.value = it },
                onFailure = { _error.value = it.message }
            )
            _loading.value = false
        }
    }

    fun loadCategorias() {
        viewModelScope.launch {
            repo.getCategorias().fold(
                onSuccess = { _categorias.value = it },
                onFailure = { /* silencioso */ }
            )
        }
    }

    fun estaEnFavoritos(idObra: Int): Boolean {
        return _favoritosIds.value?.contains(idObra) ?: false
    }

    fun toggleFavorito(idUsuario: Int, obra: Obra) {
        val listaActual = _favoritosIds.value ?: emptySet()
        val esYaFavorito = listaActual.contains(obra.idObra)

        viewModelScope.launch {
            // CORRECCIÓN: Pasamos los 3 parámetros que pide el repositorio
            val result = repo.toggleFavorito(
                idUsuario = idUsuario,
                obra = obra,
                esFavorito = esYaFavorito)

            result.fold(
                onSuccess = {
                    // Actualizamos la lista local para que el corazón cambie de color
                    val nuevaLista = if (esYaFavorito) listaActual - obra.idObra else listaActual + obra.idObra
                    _favoritosIds.value = nuevaLista
                },
                onFailure = { e ->
                    Log.e("ERROR", "No se pudo marcar favorito: ${e.message}")
                }
            )
        }
    }
    fun loadFavoritos(idUsuario: Int) {
        viewModelScope.launch {
            val result = repo.getFavoritos(idUsuario)
            result.onSuccess { listaDeFavoritos ->
                _favoritosIds.value = listaDeFavoritos.map { it.obra.idObra }.toSet()
            }.onFailure { error -> Log.e("LOAD_FAVORITOS", "Error al cargar: ${error.message}") }
            }
        }


    fun buscar(query: String) {
        viewModelScope.launch {
            repo.getObrasByTitulo(query).fold(
                onSuccess = { _obras.value = it },
                onFailure = { _error.value = it.message }
            )
        }
    }


    fun filtrarPorCategoria(idCat: Int) {
        viewModelScope.launch {
            repo.getObrasByCategoria(idCat).fold(
                onSuccess = { _obras.value = it },
                onFailure = { _error.value = it.message }
            )
        }
    }
}