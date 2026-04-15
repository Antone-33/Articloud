package com.articloud.data.repository

import com.articloud.model.*
import com.articloud.network.ApiService

class PedidosRepository(private val api: ApiService) {

    suspend fun crearPedido(idUsuario: Int, items: List<CarritoItem>): Result<Pedido> {
        val request = PedidoRequest(
            idUsuario = idUsuario,
            total = items.sumOf { it.subtotal },
            detalle = items.map { item ->
                PedidoDetalleRequest(
                    idObra = item.obra.idObra,
                    cantidad = item.cantidad,
                    precio = item.obra.precio
                )
            }
        )

        return try {
            val response = api.crearPedido(request)

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception("Error HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(Exception("Sin conexión: ${e.message}"))
        }
    }

    suspend fun getPedidosByUsuario(idUsuario: Int): Result<List<Pedido>> {
        return try {
            val response = api.getPedidosByUsuario(idUsuario)

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Lista vacía"))
            } else {
                Result.failure(Exception("Error HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(Exception("Sin conexión: ${e.message}"))
        }
    }
}