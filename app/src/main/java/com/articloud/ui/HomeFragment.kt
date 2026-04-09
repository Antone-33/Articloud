package com.articloud.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.articloud.DetailActivity
import com.articloud.LoginActivity
import com.articloud.ProfileActivity
import com.articloud.SessionManager
import com.articloud.adapter.ArtworkAdapter
import com.articloud.adapter.CategoryAdapter
import com.articloud.databinding.FragmentHomeBinding
import com.articloud.model.MockData
import com.bumptech.glide.Glide

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var artworkAdapter: ArtworkAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupCategories()
        setupArtworks()
        setupFeatured()
        setupClicks()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateHeader()
    }

    private fun setupCategories() {
        val categoryAdapter = CategoryAdapter(MockData.categories) { category, _ ->
            val filtered = if (category.name == "Todos") MockData.artworks
            else MockData.artworks.filter { it.category == category.name }
            artworkAdapter.updateList(filtered)
        }
        binding.recyclerCategories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerCategories.adapter = categoryAdapter
    }

    private fun setupArtworks() {
        artworkAdapter = ArtworkAdapter(
            list = MockData.artworks,
            onFavoriteClick = { /* futuro: guardar en favoritos */ },
            onClick = { artwork ->
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("id", artwork.id)
                intent.putExtra("name", artwork.name)
                intent.putExtra("artist", artwork.artist)
                intent.putExtra("price", artwork.price)
                intent.putExtra("originalPrice", artwork.originalPrice ?: 0.0)
                intent.putExtra("image", artwork.image)
                intent.putExtra("desc", artwork.description)
                intent.putExtra("category", artwork.category)
                intent.putExtra("size", artwork.size)
                intent.putExtra("technique", artwork.technique)
                intent.putExtra("rating", artwork.rating)
                intent.putExtra("reviewCount", artwork.reviewCount)
                intent.putExtra("hasFreeShipping", artwork.hasFreeShipping)
                startActivity(intent)
            }
        )
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = artworkAdapter
    }

    private fun setupFeatured() {
        val featured = MockData.featuredArtwork
        binding.txtFeaturedName.text = featured.name
        binding.txtFeaturedArtist.text = "por ${featured.artist}"
        binding.btnFeatured.text = "Ver obra · S/ ${String.format("%,.0f", featured.price)}"

        Glide.with(this)
            .load(featured.image)
            .centerCrop()
            .into(binding.imgFeatured)

        binding.btnFeatured.setOnClickListener {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("id", featured.id)
            intent.putExtra("name", featured.name)
            intent.putExtra("artist", featured.artist)
            intent.putExtra("price", featured.price)
            intent.putExtra("image", featured.image)
            intent.putExtra("desc", featured.description)
            intent.putExtra("category", featured.category)
            intent.putExtra("size", featured.size)
            intent.putExtra("technique", featured.technique)
            intent.putExtra("rating", featured.rating)
            intent.putExtra("reviewCount", featured.reviewCount)
            intent.putExtra("hasFreeShipping", featured.hasFreeShipping)
            startActivity(intent)
        }
    }

    private fun setupClicks() {
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
        binding.btnGuest.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
        binding.txtProfile.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileActivity::class.java))
        }
        binding.iconBell.setOnClickListener {
            // futuro: notificaciones
        }
        binding.txtVerTodas.setOnClickListener {
            // futuro: ir a explorar
        }
    }

    private fun updateHeader() {
        val isLogged = SessionManager.isLogged(requireContext())

        if (isLogged) {
            val name = SessionManager.getName(requireContext())
            binding.txtWelcome.text = "BIENVENIDO"
            binding.btnGuest.visibility = View.GONE
            binding.iconBell.visibility = View.VISIBLE
            binding.txtProfile.visibility = View.VISIBLE
            binding.txtProfile.text = name.first().uppercaseChar().toString()
            binding.layoutLogin.visibility = View.GONE
        } else {
            binding.txtWelcome.text = "EXPLORA SIN LÍMITES"
            binding.btnGuest.visibility = View.VISIBLE
            binding.iconBell.visibility = View.GONE
            binding.txtProfile.visibility = View.GONE
            binding.layoutLogin.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}