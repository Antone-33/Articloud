package com.articloud.model

data class Favorito(
    val idFavorito: Int,
    val usuario: Usuarios,
    val obra: Obra,
    val fecha: String
)