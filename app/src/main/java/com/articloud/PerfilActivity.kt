package com.articloud.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.articloud.databinding.ActivityPerfilBinding
import com.articloud.LoginActivity
import com.articloud.OrdersActivity
import com.articloud.FavoritosActivity
import com.articloud.utils.SessionManager

class PerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        cargarDatosUsuario()
        setupEventos()
    }

    // 🔹 Cargar datos del usuario
    private fun cargarDatosUsuario() {

        if (session.isLoggedIn()) {

            val nombre = session.getUserName()
            val email  = session.getUserEmail()

            // Header
            binding.tvNombre.text = nombre
            binding.tvEmail.text = email

            // Info
            binding.tvNombreInfo.text = nombre
            binding.tvEmailInfo.text = email

        } else {
            // Si no hay sesión → redirigir
            goToLogin()
        }
    }

    // 🔹 Eventos
    private fun setupEventos() {

        // 📦 Pedidos
        binding.btnPedidos.setOnClickListener {
            startActivity(Intent(this, OrdersActivity::class.java))
        }

        // ❤️ Favoritos
        binding.btnFavoritos.setOnClickListener {
            startActivity(Intent(this, FavoritosActivity::class.java))
        }

        // 🚪 Logout
        binding.btnLogout.setOnClickListener {
            session.clearSession()
            goToLogin()
        }
    }

    // 🔹 Navegación a login
    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}