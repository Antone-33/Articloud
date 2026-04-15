package com.articloud.model

data class CarritoItem(
// Item local (no persiste en BD, solo en memoria mientras la app está abierta)
    val obra: Obra,
    var cantidad: Int = 1
) {
    val subtotal: Double get() = obra.precio * cantidad
}

