package com.articloud

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.articloud.databinding.ActivityProfileBinding
import com.articloud.model.CartManager
import com.articloud.model.FavoritesManager

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    // Menú items: (id_view, icono, label)
    private val menuItems = listOf(
        Triple(R.id.menuOrders,        "📦", "Mis pedidos"),
        Triple(R.id.menuNotifications, "🔔", "Notificaciones"),
        Triple(R.id.menuPayment,       "💳", "Métodos de pago"),
        Triple(R.id.menuAddresses,     "📍", "Direcciones guardadas"),
        Triple(R.id.menuReferrals,     "🎁", "Programa de referidos"),
        Triple(R.id.menuReviews,       "⭐", "Mis reseñas"),
        Triple(R.id.menuSecurity,      "🔒", "Seguridad")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Status bar clara
        window.statusBarColor = Color.parseColor("#0D0D0D")
        window.decorView.systemUiVisibility = 0

        loadUserData()
        setupMenu()
        setupButtons()
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
    }

    private fun loadUserData() {
        val name  = SessionManager.getName(this)
        val email = SessionManager.getEmail(this)

        binding.txtName.text    = name
        binding.txtEmail.text   = email
        binding.txtInitial.text = if (name.isNotEmpty()) name.first().uppercaseChar().toString() else "?"

        // Estadísticas dinámicas
        binding.txtFavCount.text    = FavoritesManager.getCount().toString()
        binding.txtPurchaseCount.text = "0"   // futuro: desde backend
        binding.txtReviewCount.text   = "0"   // futuro: desde backend
    }

    private fun setupMenu() {
        menuItems.forEach { (viewId, icon, label) ->
            val menuView = findViewById<View>(viewId)
            menuView?.let {
                it.findViewById<TextView>(R.id.txtMenuIcon)?.text  = icon
                it.findViewById<TextView>(R.id.txtMenuLabel)?.text = label
                it.setOnClickListener {
                    Toast.makeText(this, "$label — próximamente", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupButtons() {
        binding.btnEdit.setOnClickListener {
            startActivity(android.content.Intent(this, EditProfileActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            // Limpiar sesión y datos en memoria
            SessionManager.logout(this)
            CartManager.clear()

            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            })
            finish()
        }
    }
}