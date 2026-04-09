package com.articloud.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.articloud.DetailActivity
import com.articloud.LoginActivity
import com.articloud.R
import com.articloud.SessionManager
import com.articloud.adapter.ArtworkAdapter
import com.articloud.databinding.FragmentFavoritesBinding
import com.articloud.model.FavoritesManager

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favAdapter: ArtworkAdapter

    private val favListener = { refreshFavorites() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        setupRecycler()
        setupClicks()
        FavoritesManager.addListener(favListener)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        refreshFavorites()
    }

    private fun setupRecycler() {
        favAdapter = ArtworkAdapter(
            list = FavoritesManager.getItems(),
            onFavoriteClick = { artwork ->
                FavoritesManager.toggle(artwork)
            },
            onClick = { artwork ->
                val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra("id", artwork.id)
                    putExtra("name", artwork.name)
                    putExtra("artist", artwork.artist)
                    putExtra("price", artwork.price)
                    putExtra("image", artwork.image)
                    putExtra("desc", artwork.description)
                    putExtra("category", artwork.category)
                    putExtra("size", artwork.size)
                    putExtra("technique", artwork.technique)
                    putExtra("rating", artwork.rating)
                    putExtra("reviewCount", artwork.reviewCount)
                    putExtra("hasFreeShipping", artwork.hasFreeShipping)
                }
                startActivity(intent)
            }
        )
        binding.recyclerFavorites.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerFavorites.adapter = favAdapter
    }

    private fun setupClicks() {
        binding.btnLoginFav.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
        binding.btnExploreFavGuest.setOnClickListener {
            navigateToExplore()
        }
        binding.btnExploreFavEmpty.setOnClickListener {
            navigateToExplore()
        }
    }

    private fun navigateToExplore() {
        (requireActivity() as? com.articloud.MainActivity)?.let {
            it.binding.bottomNav.selectedItemId = R.id.nav_explore
        }
    }

    private fun refreshFavorites() {
        if (_binding == null) return

        val isLogged = SessionManager.isLogged(requireContext())
        val items = FavoritesManager.getItems()

        when {
            !isLogged -> {
                binding.layoutGuest.visibility = View.VISIBLE
                binding.layoutEmpty.visibility = View.GONE
                binding.layoutContent.visibility = View.GONE
            }
            items.isEmpty() -> {
                binding.layoutGuest.visibility = View.GONE
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.layoutContent.visibility = View.GONE
            }
            else -> {
                binding.layoutGuest.visibility = View.GONE
                binding.layoutEmpty.visibility = View.GONE
                binding.layoutContent.visibility = View.VISIBLE

                favAdapter.updateList(items)
                val count = items.size
                binding.txtFavCount.text = "$count obra${if (count != 1) "s" else ""} guardada${if (count != 1) "s" else ""}"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        FavoritesManager.removeListener(favListener)
        _binding = null
    }
}