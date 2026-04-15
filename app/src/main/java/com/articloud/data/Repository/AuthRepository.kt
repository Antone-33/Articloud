package com.articloud.data.Repository

import com.articloud.model.LoginRequest
import com.articloud.model.LoginResponse
import com.articloud.model.RegisterRequest
import com.articloud.network.ApiService

class AuthRepository (private val api: ApiService) {

    suspend fun login(email: String, password: String) : Result<LoginResponse>{
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful){
                Result.success(response.body()!!)
            }else {
                val msg = when(response.code()) {
                    401 -> "Credenciales incorrectas"
                    else -> "Error desconocido: ${response.code()}"
                }
                Result.failure(Exception(msg))
            }
        } catch (e : Exception) {
            Result.failure(Exception("Sin conexion al servidor"))
        }
    }

    suspend fun register(nombre: String, email: String, password: String, telefono: String, direccion: String):
            Result<Unit> {
        return try {
            val response = api.register(
                RegisterRequest(nombre, email, password, telefono, direccion)
            )
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(
                Exception(
                    when (response.code()) {
                        409 -> "El correo ya esta registrado"
                        else -> "Error al registrar"
                    }
                )
            )
        } catch (e: Exception) {
            Result.failure(Exception("Sin conexion al servidor"))
        }
        }
    }