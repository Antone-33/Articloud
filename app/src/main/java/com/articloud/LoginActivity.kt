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

        // Inicializar binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // LOGIN
        binding.btnLogin.setOnClickListener {

            val email = binding.etUsuario.text.toString()
            val pass = binding.etPassword.text.toString()
            val name = email.substringBefore("@")

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                SessionManager.saveUser(this, name, email)

                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
        }

        //ir a registrar
        binding.txtRegistro.setOnClickListener {

            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
