package com.articloud

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.articloud.data.Repository.AuthRepository
import com.articloud.network.RetrofitClient
import com.articloud.databinding.ActivityLoginBinding
import com.articloud.ViewModels.AuthViewModel
import com.articloud.utils.SessionManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var session: SessionManager
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Barra de estado clara (la mantienes igual)
        window.statusBarColor = android.graphics.Color.WHITE
        window.decorView.systemUiVisibility =
            android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // ── Configurar SessionManager + Retrofit ──────────────
        session = SessionManager(this)

        // Si ya hay sesión activa → saltar al MainActivity
        if (session.isLoggedIn()) {
            goToMain()
            return
        }

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

    private fun setupObservers() {

        // Loading — deshabilita el botón mientras espera al backend
        viewModel.loading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !loading
        }

        // Resultado del login
        viewModel.loginResult.observe(this) { result ->
            result.fold(
                onSuccess = { resp ->
                    // ✅ Guardar sesión con datos reales del backend
                    session.saveSession(
                        token  = resp.token,
                        userId = resp.idUsuario,
                        nombre = resp.nombre,
                        email  = resp.email
                    )
                    Toast.makeText(this, "¡Bienvenido ${resp.nombre}!", Toast.LENGTH_SHORT).show()
                    goToMain()
                },
                onFailure = { e ->
                    // ❌ Mostrar error del backend
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun setupListeners() {

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass  = binding.etPassword.text.toString()

            // Validaciones (las mantienes igual)
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Ingresa un correo válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pass.length < 6) {
                Toast.makeText(this, "Contraseña muy corta", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Llamar al backend en lugar de guardar localmente
            viewModel.login(email, pass)
        }

        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Botones sociales (los mantienes igual)
        binding.btnGoogle.setOnClickListener {
            Toast.makeText(this, "Google Sign-In próximamente", Toast.LENGTH_SHORT).show()
        }
        binding.btnFacebook.setOnClickListener {
            Toast.makeText(this, "Facebook Sign-In próximamente", Toast.LENGTH_SHORT).show()
        }
        binding.btnApple.setOnClickListener {
            Toast.makeText(this, "Apple Sign-In próximamente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        })
        finish()
    }
}