package com.articloud.network

import com.articloud.model.Categoria
import com.articloud.model.Favorito
import com.articloud.model.FavoritoRequest
import com.articloud.model.LoginRequest
import com.articloud.model.LoginResponse
import com.articloud.model.Obra
import com.articloud.model.PagoRequest
import com.articloud.model.Pedido
import com.articloud.model.PedidoRequest
import com.articloud.model.RegisterRequest
import com.articloud.model.Usuarios
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //Autenticación, Login y Registro
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/usuarios/registrar")
    fun register(@Body request: RegisterRequest) : Response<Usuarios>

    //Usuarios y Perfil
    @GET("/usuarios/listar/{idUsuario}")
    fun getUsuarioById(@Path("idUsuario") id: Int): Response<Usuarios>

    @PUT("/usuarios/actualizar/{idUsuario}")
    fun updateUsuario(
        @Path("idUsuario") id: Int,
        @Body request: RegisterRequest,
        usuario: RegisterRequest
    ): Response<Usuarios>

    //Obras
    @GET("/obras/listar")
    fun getObras(): Response<List<Obra>>

    @GET("/obras/listar/{idObra}")
    fun getObrasById(@Path("idObra") id: Int) : Response<Obra>

    @GET("/obras/listar/titulo/{titulo}")
    fun getObrasByTitulo(@Path("titulo") titulo: String): Response<List<Obra>>

    @GET("/obras/listar/precio/{precio}")
    fun getObrasByPrecio(@Path("precio") precio: Double): Response<List<Obra>>

    @GET("/obras/listar/categoria/{idCategoria}")
    fun getObrasByCategoria(@Path("idCategoria") idCategoria: Int): Response<List<Obra>>

    //Categorias
    @GET("/categorias/listar")
    fun getCategorias(): Response<List<Categoria>>

    //Pedidos

    @GET("pedidos/buscar/usuario/{idUsuario}")
    suspend fun getPedidosByUsuario( @Path("idUsuario") idUsuario: Int): Response<List<Pedido>>

    // POST /api/pedidos/guardar
    @POST("pedidos/crear")
    suspend fun crearPedido(@Body request: PedidoRequest): Response<Pedido>

    //Favoritos
    // GET /api/favoritos/usuario/{idUsuario}
    @GET("favoritos/usuario/{idUsuario}")
    suspend fun getFavoritos(
        @Path("idUsuario") idUsuario: Int
    ): Response<List<Favorito>>

    // POST /api/favoritos/agregar
    @POST("favoritos/agregar")
    suspend fun agregarFavorito(
        @Body request: FavoritoRequest
    ): Response<Favorito>

    // DELETE /api/favoritos/eliminar
    @DELETE("favoritos/eliminar")
    suspend fun eliminarFavorito(
        @Body request: FavoritoRequest
    ): Response<String>

    // GET /api/favoritos/verificar
    @GET("favoritos/verificar")
    suspend fun verificarFavorito(
        @Query("idUsuario") idUsuario: Int,
        @Query("idObra")    idObra: Int
    ): Response<Boolean>

    @POST("api/pagos/procesar")
    suspend fun procesarPago(
        @Body request: PagoRequest
    ): Response<String> //
}

