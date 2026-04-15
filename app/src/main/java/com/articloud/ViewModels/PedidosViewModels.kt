package com.articloud.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.articloud.data.repository.PedidosRepository
import com.articloud.model.Pedido
import kotlinx.coroutines.launch

class PedidosViewModels(private val repo: PedidosRepository) : ViewModel() {

    private val _pedidos = MutableLiveData<List<Pedido>>()
    val pedidos: LiveData<List<Pedido>> = _pedidos

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    // GET /api/pedidos/buscar/usuario/{idUsuario}
    fun cargarPedidos(idUsuario: Int) {
        _loading.value = true
        viewModelScope.launch {
            repo.getPedidosByUsuario(idUsuario).fold(
                onSuccess = { _pedidos.value = it },
                onFailure = { _error.value = it.message }
            )
            _loading.value = false
        }
    }
}