package com.articloud.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.articloud.model.CarritoItem
import com.articloud.model.Obra


class CarViewModel : ViewModel() {

    private val _items = MutableLiveData<List<CarritoItem>>(emptyList())
    val items: LiveData<List<CarritoItem>> = _items

    val totalItems: Int get() = _items.value?.sumOf { it.cantidad } ?: 0
    val totalPrice: Double get() = _items.value?.sumOf { it.subtotal } ?: 0.0

    fun agregar(obra: Obra, cantidad: Int = 1) {
        val current = _items.value.orEmpty().toMutableList()
        val index = current.indexOfFirst { it.obra.idObra == obra.idObra }
        if (index != -1) {
            val item = current[index]
            current[index] = item.copy(cantidad = item.cantidad + cantidad)
        } else {
            current.add(CarritoItem(obra, cantidad))
        }
        _items.value = current
        }

        fun quitar(idObra: Int) {
            val updated = _items.value.orEmpty()
                .filter { it.obra.idObra != idObra }

            _items.value = updated
        }

    fun cambiarCantidad(idObra: Int, nuevaCantidad: Int) {
        val lista = _items.value ?: return
        lista.find { it.obra.idObra == idObra }?.cantidad = nuevaCantidad
        _items.value = lista
    }

    fun limpiar() { _items.value = mutableListOf() }

    fun getItems(): List<CarritoItem> = _items.value ?: emptyList()
}
