package com.articloud.data.Repository

import com.articloud.model.Categoria
import com.articloud.model.Favorito
import com.articloud.model.FavoritoRequest
import com.articloud.model.Obra
import com.articloud.network.ApiService
import retrofit2.Response

class ObrasRepository(private val api: ApiService) {

    suspend fun getObras(): Result<List<Obra>> = safeCall { api.getObras() }

    suspend fun getObrasById(id: Int): Result<Obra> = safeCall { api.getObrasById(id) }

    suspend fun getObrasByTitulo(titulo: String): Result<List<Obra>> =
        safeCall { api.getObrasByTitulo(titulo) }

    suspend fun getObrasByCategoria(idCategoria: Int): Result<List<Obra>> =
        safeCall { api.getObrasByCategoria(idCategoria) }

    suspend fun getFavoritos(idUsuario: Int): Result<List<Favorito>> {
        return try {
            val response = api.getFavoritos(idUsuario)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error en el servidor"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getCategorias(): Result<List<Categoria>> = safeCall { api.getCategorias() }


    suspend fun toggleFavorito(idUsuario: Int, obra: Obra, esFavorito: Boolean): Result<Unit> {
        return try {
            val response = if (esFavorito) {
                val request = FavoritoRequest(idUsuario, obra.idObra)
                // Si ya es favorito, lo quitamos
                api.eliminarFavorito(request)
            } else {
                // Si no lo es, lo agregamos
                val request = FavoritoRequest(idUsuario, obra.idObra)
                api.agregarFavorito(request)
            }

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error en el servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
    private suspend fun <T> safeCall(call: suspend () -> retrofit2.Response<T>): Result<T> {
        return try {
            val r = call()
            if (r.isSuccessful) Result.success(r.body()!!)
            else Result.failure(Exception("Error ${r.code()}"))
        } catch (e: Exception) {
            Result.failure(Exception("Sin conexión"))
        }
    }

