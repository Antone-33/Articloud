package com.articloud

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.articloud.network.RetrofitClient
import com.articloud.data.Repository.AuthRepository
import com.articloud.databinding.ActivityRegisterBinding
import com.articloud.ViewModels.AuthViewModel
import com.articloud.utils.SessionManager

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var session: SessionManager
    private lateinit var viewModel: AuthViewModel
    private var passwordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Barra de estado igual que login
        window.statusBarColor = android.graphics.Color.parseColor("#0D0D0D")

        // ── Configurar SessionManager + Retrofit ──────────────
        session = SessionManager(this)
        val api  = RetrofitClient.create(session)
        val repo = AuthRepository(api)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(repo) as T
            }
        })[AuthViewModel::class.java]

        setupObservers()
        setupListeners()
    }

    /* ══════════════════════════════════════════
       OBSERVERS
    ══════════════════════════════════════════ */
    private fun setupObservers() {

        // Loading
        viewModel.loading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnRegistrar.isEnabled = !loading
            binding.btnRegistrar.alpha     = if (loading) 0.6f else 1.0f
        }

        // Resultado del registro
        viewModel.registerResult.observe(this) { result ->
            result.fold(
                onSuccess = {
                    // ✅ Usuario guardado en BD → hacer login automático
                    viewModel.login(
                        email    = binding.etEmail.text.toString().trim(),
                        password = binding.etPassword.text.toString()
                    )
                },
                onFailure = { e ->
                    binding.tvErrorGeneral.text       = e.message
                    binding.tvErrorGeneral.visibility = View.VISIBLE
                }
            )
        }

        // Login automático después del registro
        viewModel.loginResult.observe(this) { result ->
            result.fold(
                onSuccess = { resp ->
                    // Guardar sesión con datos reales del backend
                    session.saveSession(
                        token  = resp.token,
                        userId = resp.idUsuario,
                        nombre = resp.nombre,
                        email  = resp.email
                    )
                    Toast.makeText(
                        this,
                        "¡Bienvenido ${resp.nombre}!",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Ir al MainActivity limpiando el stack
                    startActivity(
                        Intent(this, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                    Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    )
                    finishAffinity()
                },
                onFailure = {
                    // Si el login falla → ir manualmente al login
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            )
        }
    }

    /* ══════════════════════════════════════════
       LISTENERS
    ══════════════════════════════════════════ */
    private fun setupListeners() {

        // Volver atrás
        binding.btnBack.setOnClickListener { finish() }

        // Ir al login
        binding.tvIrLogin.setOnClickListener { finish() }

        // Mostrar / ocultar contraseña
        binding.btnTogglePass.setOnClickListener {
            passwordVisible = !passwordVisible
            binding.etPassword.inputType = if (passwordVisible) {
                android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                android.text.InputType.TYPE_CLASS_TEXT or
                        android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            // Mover cursor al final
            binding.etPassword.setSelection(binding.etPassword.text.length)
        }

        // Indicador de seguridad de contraseña
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}
            override fun afterTextChanged(s: Editable?) {
                actualizarSeguridad(s.toString())
            }
        })

        // Botón registrarse
        binding.btnRegistrar.setOnClickListener {
            if (validarCampos()) {
                ocultarErrores()
                viewModel.register(
                    nombre    = binding.etNombre.text.toString().trim(),
                    email     = binding.etEmail.text.toString().trim(),
                    password  = binding.etPassword.text.toString(),
                    telefono  = binding.etTelefono.text.toString().trim(),
                    direccion = binding.etDireccion.text.toString().trim()
                )
            }
        }
    }

    /* ══════════════════════════════════════════
       VALIDACIONES
    ══════════════════════════════════════════ */
    private fun validarCampos(): Boolean {
        var valido = true
        ocultarErrores()

        val nombre    = binding.etNombre.text.toString().trim()
        val email     = binding.etEmail.text.toString().trim()
        val password  = binding.etPassword.text.toString()

        if (nombre.isEmpty()) {
            binding.tvErrorNombre.visibility = View.VISIBLE
            binding.etNombre.requestFocus()
            valido = false
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tvErrorEmail.visibility = View.VISIBLE
            if (valido) binding.etEmail.requestFocus()
            valido = false
        }

        if (password.length < 6) {
            binding.tvErrorPassword.visibility = View.VISIBLE
            if (valido) binding.etPassword.requestFocus()
            valido = false
        }

        return valido
    }

    private fun ocultarErrores() {
        binding.tvErrorNombre.visibility   = View.GONE
        binding.tvErrorEmail.visibility    = View.GONE
        binding.tvErrorPassword.visibility = View.GONE
        binding.tvErrorGeneral.visibility  = View.GONE
    }

    /* ══════════════════════════════════════════
       INDICADOR DE SEGURIDAD DE CONTRASEÑA
    ══════════════════════════════════════════ */
    private fun actualizarSeguridad(password: String) {
        if (password.isEmpty()) {
            resetBarras()
            binding.tvStrength.visibility = View.GONE
            return
        }

        binding.tvStrength.visibility = View.VISIBLE

        val nivel = when {
            password.length >= 10 &&
                    password.any { it.isDigit() } &&
                    password.any { it.isUpperCase() } -> 3  // Fuerte

            password.length >= 6 &&
                    (password.any { it.isDigit() } ||
                            password.any { it.isUpperCase() }) -> 2  // Media

            else -> 1  // Débil
        }

        when (nivel) {
            1 -> {
                binding.strengthBar1.setBackgroundColor(0xFFE53935.toInt())
                binding.strengthBar2.setBackgroundColor(0xFFE0E0E0.toInt())
                binding.strengthBar3.setBackgroundColor(0xFFE0E0E0.toInt())
                binding.tvStrength.text      = "Débil"
                binding.tvStrength.setTextColor(0xFFE53935.toInt())
            }
            2 -> {
                binding.strengthBar1.setBackgroundColor(0xFFFFA726.toInt())
                binding.strengthBar2.setBackgroundColor(0xFFFFA726.toInt())
                binding.strengthBar3.setBackgroundColor(0xFFE0E0E0.toInt())
                binding.tvStrength.text      = "Media"
                binding.tvStrength.setTextColor(0xFFFFA726.toInt())
            }
            3 -> {
                binding.strengthBar1.setBackgroundColor(0xFF43A047.toInt())
                binding.strengthBar2.setBackgroundColor(0xFF43A047.toInt())
                binding.strengthBar3.setBackgroundColor(0xFF43A047.toInt())
                binding.tvStrength.text      = "Fuerte"
                binding.tvStrength.setTextColor(0xFF43A047.toInt())
            }
        }
    }

    private fun resetBarras() {
        listOf(binding.strengthBar1, binding.strengthBar2, binding.strengthBar3)
            .forEach { it.setBackgroundColor(0xFFE0E0E0.toInt()) }
    }
}