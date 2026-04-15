package com.articloud.model

data class RegisterRequest(
    val nombre: String,
    val email: String,
    val password: String,
    val telefono: String,
    val direccion: String
)
