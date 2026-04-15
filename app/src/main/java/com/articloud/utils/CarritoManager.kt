package com.articloud.data.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.articloud.model.CarritoItem
import com.articloud.model.Obra

object CarritoManager {

    // 🧠 Fuente de verdad
    private val _items = MutableLiveData<List<CarritoItem>>(emptyList())
    val items: LiveData<List<CarritoItem>> = _items

    // ============================
    // ➕ AGREGAR ITEM
    // ============================
    fun agregarItem(obra: Obra) {
        val lista = _items.value.orEmpty().toMutableList()

        val index = lista.indexOfFirst { it.obra.idObra == obra.idObra }

        if (index != -1) {
            val item = lista[index]
            lista[index] = item.copy(cantidad = item.cantidad + 1)
        } else {
            lista.add(CarritoItem(obra, 1))
        }

        _items.value = lista
    }

    // ============================
    // ➖ DISMINUIR / ACTUALIZAR
    // ============================
    fun actualizarCantidad(idObra: Int, cantidad: Int) {
        val lista = _items.value.orEmpty().toMutableList()

        val index = lista.indexOfFirst { it.obra.idObra == idObra }

        if (index != -1) {
            val item = lista[index]
            lista[index] = item.copy(cantidad = cantidad)
        }

        _items.value = lista
    }

    // ============================
    // 🗑️ ELIMINAR ITEM
    // ============================
    fun eliminarItem(idObra: Int) {
        val lista = _items.value.orEmpty()
            .filter { it.obra.idObra != idObra }

        _items.value = lista
    }

    // ============================
    // 🧹 LIMPIAR CARRITO
    // ============================
    fun limpiar() {
        _items.postValue(emptyList())
    }
    // ============================
    // 📊 TOTALES
    // ============================
    fun getTotal(): Double =
        _items.value.orEmpty().sumOf { it.subtotal }

    fun getCantidadTotal(): Int =
        _items.value.orEmpty().sumOf { it.cantidad }

    fun getItemsSnapshot(): List<CarritoItem> {
        return _items.value?.toList() ?: emptyList()
    }
}