package com.articloud

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.articloud.ui.catalog.CatalogActivity
import com.articloud.utils.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inicializar el SessionManager para verificar el estado del usuario
        session = SessionManager(this)

            // El CarritoManager ya existe globalmente, no requiere inicialización manual.
        if (session.isLoggedIn()) {
                startActivity(Intent(this, CatalogActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }
    }
