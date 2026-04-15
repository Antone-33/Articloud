package com.articloud.model

data class PagoRequest(
    val idPedido: Int,
    val monto: Double,
    val metodoPago: MetodoPago = MetodoPago.TARJETA_DEBITO
)

