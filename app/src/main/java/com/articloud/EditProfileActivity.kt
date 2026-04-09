package com.articloud

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.articloud.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.parseColor("#0D0D0D")
        window.decorView.systemUiVisibility = 0

        loadData()
        setupButtons()
    }

    private fun loadData() {
        val name  = SessionManager.getName(this)
        val email = SessionManager.getEmail(this)
        val phone = SessionManager.getPhone(this)
        val dni   = SessionManager.getDni(this)
        val addr  = SessionManager.getAddress(this)
        val city  = SessionManager.getCity(this)
        val bio   = SessionManager.getBio(this)

        // Header
        binding.txtInitial.text    = if (name.isNotEmpty()) name.first().uppercaseChar().toString() else "?"
        binding.txtNameHeader.text = name
        binding.txtEmailHeader.text = email

        // Email (no editable)
        binding.txtEmailDisplay.text = email

        // Campos editables
        binding.etName.setText(name)
        if (phone.isNotEmpty()) binding.etPhone.setText(phone)
        if (dni.isNotEmpty())   binding.etDni.setText(dni)
        if (addr.isNotEmpty())  binding.etAddress.setText(addr)
        if (city.isNotEmpty())  binding.etCity.setText(city)
        if (bio.isNotEmpty())   binding.etBio.setText(bio)
    }

    private fun setupButtons() {
        binding.btnBack.setOnClickListener { finish() }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString().trim()

            if (name.isEmpty()) {
                binding.etName.error = "El nombre es requerido"
                return@setOnClickListener
            }

            val email = SessionManager.getEmail(this)

            // Guardar todo en SessionManager
            SessionManager.saveUser(this, name, email)
            SessionManager.saveProfile(
                context   = this,
                phone     = binding.etPhone.text.toString().trim(),
                dni       = binding.etDni.text.toString().trim(),
                address   = binding.etAddress.text.toString().trim(),
                city      = binding.etCity.text.toString().trim(),
                bio       = binding.etBio.text.toString().trim()
            )

            Toast.makeText(this, "✓ Perfil actualizado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}