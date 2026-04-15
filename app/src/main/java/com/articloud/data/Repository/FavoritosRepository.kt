package com.articloud.data.Repository

import com.articloud.model.Favorito
import com.articloud.model.FavoritoRequest
import com.articloud.network.ApiService

class FavoritosRepository(private val api: ApiService) {

    // GET /api/favoritos/usuario/{idUsuario}
    suspend fun getFavoritos(idUsuario: Int): Result<List<Favorito>> {
        return try {
            val r = api.getFavoritos(idUsuario)
            if (r.isSuccessful) Result.success(r.body()!!)
            else Result.failure(Exception("Error al cargar favoritos"))
        } catch (e: Exception) {
            Result.failure(Exception("Sin conexión"))
        }
    }

    // POST /api/favoritos/agregar
    suspend fun agregar(idUsuario: Int, idObra: Int): Result<Favorito> {
        return try {
            val r = api.agregarFavorito(FavoritoRequest(idUsuario, idObra))
            if (r.isSuccessful) Result.success(r.body()!!)
            else Result.failure(Exception("Error al agregar favorito"))
        } catch (e: Exception) {
            Result.failure(Exception("Sin conexión"))
        }
    }

    // DELETE /api/favoritos/eliminar
    suspend fun eliminar(idUsuario: Int, idObra: Int): Result<Unit> {
        return try {
            val r = api.eliminarFavorito(FavoritoRequest(idUsuario, idObra))
            if (r.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error al eliminar favorito"))
        } catch (e: Exception) {
            Result.failure(Exception("Sin conexión"))
        }
    }
}
