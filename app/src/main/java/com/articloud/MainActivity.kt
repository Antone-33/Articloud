package com.articloud

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.articloud.adapter.ArtworkAdapter
import com.articloud.databinding.ActivityMainBinding
import com.articloud.model.Artwork
import com.articloud.ui.CartFragment
import com.articloud.ui.HomeFragment
import com.articloud.ui.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, ProfileFragment())
                        .commit()
                }
            }

            true
        }
// se deja de usar porque los recicler views pasan a fragments
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//        binding.recyclerView.adapter = adapter
    }
}