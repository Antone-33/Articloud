package com.articloud.model

data class Usuarios (
    val idUsuario: Int,
    val nombre: String,
    val email: String,
    val password: String,
    val telefono: String,
    val direccion: String,
    val fechaRegistro: String
)