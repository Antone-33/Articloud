package com.articloud

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.articloud.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = SessionManager.getName(this)
        val email = SessionManager.getEmail(this)

        binding.txtName.text = name
        binding.txtEmail.text = email
        binding.txtInitial.text = name.first().toString()

        binding.btnLogout.setOnClickListener {

            SessionManager.logout(this)

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}