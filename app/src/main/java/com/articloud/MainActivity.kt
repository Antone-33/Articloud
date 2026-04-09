package com.articloud

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.articloud.databinding.ActivityMainBinding
import com.articloud.ui.CartFragment
import com.articloud.ui.ExploreFragment
import com.articloud.ui.FavoritesFragment
import com.articloud.ui.HomeFragment

class MainActivity : AppCompatActivity() {

    internal lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fragmento inicial
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment())
                .commit()
            binding.bottomNav.selectedItemId = R.id.nav_home
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, HomeFragment())
                        .commit()
                }

                R.id.nav_explore -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, ExploreFragment())
                        .commit()
                }

                R.id.nav_fav -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, FavoritesFragment())
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
                    return@setOnItemSelectedListener false
                }
            }
            true
        }
    }
}