package com.articloud.model

data class LoginResponse(
    val token: String,
    val idUsuario: Int,
    val nombre: String,
    val email: String
)
