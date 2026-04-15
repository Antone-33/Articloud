package com.articloud

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.articloud.ui.catalog.CatalogActivity
import com.articloud.utils.SessionManager

class LineArtActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lineart)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ivlineart)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val session = SessionManager(this)
        Handler(Looper.getMainLooper()).postDelayed({
            if (session.isLoggedIn()) {
                startActivity(Intent(this, CatalogActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 12290)
    }
}
