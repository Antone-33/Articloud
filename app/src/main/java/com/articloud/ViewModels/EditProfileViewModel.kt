package com.articloud.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.articloud.network.ApiService
import kotlinx.coroutines.launch
import com.articloud.model.RegisterRequest
import com.articloud.model.Usuarios

class EditProfileViewModel(private val api: ApiService) : ViewModel() {

    private val _usuario       = MutableLiveData<Usuarios>()
    val usuario: LiveData<Usuarios> = _usuario

    private val _loading       = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> = _updateSuccess

    private val _error         = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    // GET /api/usuarios/listar/{idUsuario}
    fun cargarUsuario(idUsuario: Int) {
        viewModelScope.launch {
            try {
                val r = api.getUsuarioById(idUsuario)
                if (r.isSuccessful) _usuario.value = r.body()
                else _error.value = "Error al cargar perfil"
            } catch (e: Exception) {
                _error.value = "Sin conexión al servidor"
            }
        }
    }

    // PUT /api/usuarios/actualizar/{idUsuario}
    fun actualizarPerfil(
        idUsuario : Int,
        nombre    : String,
        email     : String,
        telefono  : String,
        direccion : String
    ) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val r = api.updateUsuario(
                    id = idUsuario,
                    usuario = RegisterRequest(
                        nombre = nombre,
                        email = email,
                        password = "",     // no se actualiza la contraseña aquí
                        telefono = telefono,
                        direccion = direccion
                    ),
                    request = TODO()
                )
                if (r.isSuccessful) _updateSuccess.value = true
                else _error.value = "Error al actualizar perfil"
            } catch (e: Exception) {
                _error.value = "Sin conexión al servidor"
            }
            _loading.value = false
        }
    }
}