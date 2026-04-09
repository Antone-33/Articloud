package com.articloud

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.articloud.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()
        setupTabs()
        setupButtons()
    }

    private fun loadData() {
        val name       = intent.getStringExtra("name") ?: ""
        val artist     = intent.getStringExtra("artist") ?: ""
        val price      = intent.getDoubleExtra("price", 0.0)
        val image      = intent.getStringExtra("image")
        val desc       = intent.getStringExtra("desc") ?: ""
        val category   = intent.getStringExtra("category") ?: ""
        val size       = intent.getStringExtra("size") ?: ""
        val technique  = intent.getStringExtra("technique") ?: ""
        val year       = intent.getIntExtra("year", 0)
        val rating     = intent.getFloatExtra("rating", 0f)
        val reviews    = intent.getIntExtra("reviewCount", 0)
        val freeShip   = intent.getBooleanExtra("hasFreeShipping", false)

        // Header
        binding.txtMeta.text = "${category.uppercase()} · ${size.uppercase()} · ${technique.uppercase()}"
        binding.txtNameDetail.text = name
        binding.txtArtistDetail.text = "por $artist"
        binding.txtPriceDetail.text = "S/ ${String.format("%,.0f", price)}"
        binding.txtRatingDetail.text = rating.toString()
        binding.txtReviewsDetail.text = "($reviews)"

        // Tags
        binding.tagSize.text = "📐 $size"
        binding.tagTechnique.text = "🎨 $technique"
        binding.tagYear.text = "📅 $year"

        // Envío gratis
        binding.layoutFreeShipping.visibility = if (freeShip) View.VISIBLE else View.GONE

        // Descripción
        binding.txtDescription.text = desc

        // Tab Detalles
        binding.detailTechnique.text = technique
        binding.detailSize.text = size
        binding.detailYear.text = year.toString()
        binding.detailCategory.text = category
        binding.detailShipping.text = if (freeShip) "Gratis" else "A calcular"

        // Tab Reseñas
        binding.txtReviewScore.text = "$rating / 5.0"
        binding.txtReviewCount.text = "$reviews reseñas"

        // Imagen
        Glide.with(this).load(image).centerCrop().into(binding.imgDetail)

        // Botón según sesión
        if (SessionManager.isLogged(this)) {
            binding.btnAddCart.text = "Agregar al carrito · S/ ${String.format("%,.0f", price)}"
        } else {
            binding.btnAddCart.text = "🔒 Inicia sesión para comprar"
        }
    }

    private fun setupTabs() {
        // Estado inicial: Descripción activa
        setActiveTab(binding.tabDescription)

        binding.tabDescription.setOnClickListener {
            setActiveTab(binding.tabDescription)
            binding.contentDescription.visibility = View.VISIBLE
            binding.contentDetails.visibility = View.GONE
            binding.contentReviews.visibility = View.GONE
        }
        binding.tabDetails.setOnClickListener {
            setActiveTab(binding.tabDetails)
            binding.contentDescription.visibility = View.GONE
            binding.contentDetails.visibility = View.VISIBLE
            binding.contentReviews.visibility = View.GONE
        }
        binding.tabReviews.setOnClickListener {
            setActiveTab(binding.tabReviews)
            binding.contentDescription.visibility = View.GONE
            binding.contentDetails.visibility = View.GONE
            binding.contentReviews.visibility = View.VISIBLE
        }
    }

    private fun setActiveTab(active: TextView) {
        val tabs = listOf(binding.tabDescription, binding.tabDetails, binding.tabReviews)
        tabs.forEach { tab ->
            if (tab == active) {
                tab.setTextColor(Color.WHITE)
                tab.textSize = 14f
                // underline
                tab.paintFlags = tab.paintFlags or android.graphics.Paint.UNDERLINE_TEXT_FLAG
            } else {
                tab.setTextColor(Color.parseColor("#666666"))
                tab.textSize = 14f
                tab.paintFlags = tab.paintFlags and android.graphics.Paint.UNDERLINE_TEXT_FLAG.inv()
            }
        }
    }

    private fun setupButtons() {
        binding.btnBack.setOnClickListener { finish() }

        binding.btnShare.setOnClickListener {
            Toast.makeText(this, "Compartir próximamente", Toast.LENGTH_SHORT).show()
        }

        binding.btnAddCart.setOnClickListener {
            if (SessionManager.isLogged(this)) {
                // Buscar la obra en MockData por id y agregarla al carrito
                val id = intent.getIntExtra("id", -1)
                val artwork = com.articloud.model.MockData.artworks.find { it.id == id }
                if (artwork != null) {
                    com.articloud.model.CartManager.addItem(artwork)
                    Toast.makeText(this, "✓ Agregado al carrito", Toast.LENGTH_SHORT).show()
                    // Actualizar texto del botón
                    binding.btnAddCart.text = "✓ En el carrito"
                }
            } else {
                startActivity(android.content.Intent(this, LoginActivity::class.java))
            }
        }
    }
}