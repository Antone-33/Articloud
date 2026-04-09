package com.articloud

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.articloud.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Barra de estado clara (fondo blanco)
        window.statusBarColor = android.graphics.Color.WHITE
        window.decorView.systemUiVisibility =
            android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass  = binding.etPassword.text.toString()

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

            val name = email.substringBefore("@")
            SessionManager.saveUser(this, name, email)

            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            })
            finish()
        }

        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Botones sociales (placeholder)
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
}