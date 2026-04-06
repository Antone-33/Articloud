package com.articloud

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.articloud.adapter.ArtworkAdapter
import com.articloud.databinding.ActivityMainBinding
import com.articloud.model.Artwork
import com.articloud.ui.CartFragment
import com.articloud.ui.FavoritesFragment
import com.articloud.ui.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Seleccionar Inicio por defecto
        binding.bottomNav.selectedItemId = R.id.nav_home

        // navegacion
        binding.bottomNav.setOnItemSelectedListener {

            when(it.itemId) {

                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, HomeFragment())
                        .commit()
                }

                R.id.nav_cart -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, CartFragment())
                        .commit()
                }

                R.id.nav_profile -> {

                    if (SessionManager.isLogged(this)) {
                        startActivity(Intent(this, ProfileActivity::class.java))
                    } else {
                        startActivity(Intent(this, LoginActivity::class.java))
                    }

                }

                R.id.nav_fav -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, FavoritesFragment())
                    .commit()


            }

            true
        }


        //Contador de carrito..........
        val badge = binding.bottomNav.getOrCreateBadge(R.id.nav_cart)
        badge.isVisible = true
        badge.number = 2
        badge.backgroundColor = getColor(R.color.gold)
        badge.badgeTextColor = getColor(R.color.black_main)

        val artworks = listOf(
            Artwork(
                1,
                "Cuadro Abstracto",
                120.0,
                "https://pymstatic.com/143552/conversions/el-arte-siempre-ha-sido-trascendental-wide_webp.webp",
                "Arte moderno"
            ),
            Artwork(
                2,
                "Paisaje",
                200.0,
                "https://media.admagazine.com/photos/618a6acacc7069ed5077ca7c/16:9/w_1920,c_limit/69052.jpg",
                "Naturaleza"
            )
        )
        val adapter = ArtworkAdapter(artworks) { artwork ->

            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("name", artwork.name)
            intent.putExtra("price", artwork.price)
            intent.putExtra("image", artwork.image)
            intent.putExtra("desc", artwork.description)

            startActivity(intent)
        }


// se deja de usar porque los recicler views pasan a fragments
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//        binding.recyclerView.adapter = adapter
    }

}