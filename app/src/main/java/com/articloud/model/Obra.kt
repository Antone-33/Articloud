package com.articloud.model

data class Obra(
    val idObra: Int,
    val titulo: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val image_url: String,
    val idCategoria: Int,
    val fechaRegistro: String,
    var esFavorito: Boolean = false
)
