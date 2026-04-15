package com.articloud.model

data class PedidoRequest(
    val idUsuario: Int,
    val total: Double,
    val estado: EstadoPedido = EstadoPedido.PENDIENTE,
    val detalle: List<PedidoDetalleRequest>
)
