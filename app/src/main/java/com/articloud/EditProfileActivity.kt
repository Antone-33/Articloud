package com.articloud

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.articloud.ViewModels.EditProfileViewModel
import com.articloud.network.RetrofitClient
import com.articloud.databinding.ActivityEditProfileBinding
import com.articloud.utils.SessionManager

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var session: SessionManager
    private lateinit var viewModel: EditProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.parseColor("#0D0D0D")

        session = SessionManager(this)

        val api = RetrofitClient.create(session)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return EditProfileViewModel(api) as T
            }
        })[EditProfileViewModel::class.java]

        cargarDatosActuales()
        setupObservers()
        setupListeners()
    }

    /* ══════════════════════════════════════════
      CARGAR DATOS ACTUALES DEL SESSIONMANAGER
   ══════════════════════════════════════════ */
    private fun cargarDatosActuales() {
        val nombre = session.getUserName()
        val email  = session.getUserEmail()

        // Header avatar — inicial del nombre
        binding.txtInitial.text     = nombre.firstOrNull()?.uppercaseChar()?.toString() ?: "A"
        binding.txtNameHeader.text  = nombre
        binding.txtEmailHeader.text = email

        // Email solo lectura (no editable)
        binding.txtEmailDisplay.text = email

        // Pre-rellenar campos editables
        binding.etName.setText(nombre)

        // GET /api/usuarios/listar/{idUsuario} para cargar
        // teléfono y dirección actuales
        viewModel.cargarUsuario(session.getUserId())
    }

    /* ══════════════════════════════════════════
       OBSERVERS
    ══════════════════════════════════════════ */
    private fun setupObservers() {

        // Loading
        viewModel.loading.observe(this) { loading ->
            binding.btnSave.isEnabled = !loading
            binding.btnSave.text      = if (loading) "Guardando..." else "✓ Guardar"
        }

        // Usuario cargado → rellenar campos
        viewModel.usuario.observe(this) { usuario ->
            binding.etName.setText(usuario.nombre)
            binding.etPhone.setText(usuario.telefono ?: "")
            binding.etAddress.setText(usuario.direccion ?: "")

            // Actualizar header con datos frescos
            binding.txtInitial.text    = usuario.nombre.firstOrNull()
                ?.uppercaseChar()?.toString() ?: "A"
            binding.txtNameHeader.text = usuario.nombre
        }

        // Actualización exitosa
        viewModel.updateSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(
                    this,
                    "Perfil actualizado correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                // Actualizar el nombre en SessionManager
                // (el email no cambia, viene del backend)
                val nuevoNombre = binding.etName.text.toString().trim()
                session.saveSession(
                    token  = session.getToken() ?: "",
                    userId = session.getUserId(),
                    nombre = nuevoNombre,
                    email  = session.getUserEmail()
                )

                finish()
            }
        }

        // Error
        viewModel.error.observe(this) { msg ->
            msg?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    /* ══════════════════════════════════════════
       LISTENERS
    ══════════════════════════════════════════ */
    private fun setupListeners() {

        // Volver
        binding.btnBack.setOnClickListener { finish() }

        // Guardar cambios
        binding.btnSave.setOnClickListener {
            guardarCambios()
        }
    }

    /* ══════════════════════════════════════════
       VALIDAR Y GUARDAR
    ══════════════════════════════════════════ */
    private fun guardarCambios() {
        val nombre    = binding.etName.text.toString().trim()
        val telefono  = binding.etPhone.text.toString().trim()
        val direccion = binding.etAddress.text.toString().trim()

        // Validar nombre obligatorio
        if (nombre.isEmpty()) {
            binding.etName.error = "El nombre es requerido"
            binding.etName.requestFocus()
            return
        }

        // PUT /api/usuarios/actualizar/{idUsuario}
        viewModel.actualizarPerfil(
            idUsuario = session.getUserId(),
            nombre    = nombre,
            email     = session.getUserEmail(),  // email no cambia
            telefono  = telefono,
            direccion = direccion
        )
    }
}
