package com.articloud

import android.os.Bundle
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

        val name = intent.getStringExtra("name")
        val price = intent.getDoubleExtra("price", 0.0)
        val image = intent.getStringExtra("image")
        val desc = intent.getStringExtra("desc")

        binding.txtNameDetail.text = name
        binding.txtPriceDetail.text = "$$price"
        binding.txtDescription.text = desc
        binding.btnAddCart.setOnClickListener {
            Toast.makeText(this, "Agregado al carrito 🛒", Toast.LENGTH_SHORT).show()
        }

        Glide.with(this)
            .load(image)
            .into(binding.imgDetail)
    }
}