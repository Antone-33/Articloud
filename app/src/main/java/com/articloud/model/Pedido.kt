package com.articloud.model

data class Pedido(
    val idPedido: Int,
    val idUsuario: Int,
    val fechaRegistro: String,
    val total: Double,
    val estado: EstadoPedido
)
