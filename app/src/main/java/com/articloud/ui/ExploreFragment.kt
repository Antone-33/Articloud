package com.articloud.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.articloud.DetailActivity
import com.articloud.R
import com.articloud.adapter.ArtworkAdapter
import com.articloud.databinding.FragmentExploreBinding
import com.articloud.model.Artwork
import com.articloud.model.MockData

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private lateinit var artworkAdapter: ArtworkAdapter
    private var currentFilter = FilterState()
    private var currentQuery = ""

    private val chipCategories = listOf("Todos", "Abstracto", "Paisaje", "Figurativo", "Urbano")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        setupChips()
        setupRecycler()
        setupSearch()
        applyFilters()
        return binding.root
    }

    private fun setupChips() {
        binding.chipGroup.removeAllViews()
        chipCategories.forEach { label ->
            val chip = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_chip, binding.chipGroup, false) as TextView
            chip.text = label
            val isSelected = label == currentFilter.category
            updateChipStyle(chip, isSelected)
            chip.setOnClickListener {
                currentFilter = currentFilter.copy(category = label)
                refreshChips()
                applyFilters()
            }
            binding.chipGroup.addView(chip)
        }
    }

    private fun refreshChips() {
        for (i in 0 until binding.chipGroup.childCount) {
            val chip = binding.chipGroup.getChildAt(i) as TextView
            updateChipStyle(chip, chip.text == currentFilter.category)
        }
    }

    private fun updateChipStyle(chip: TextView, isSelected: Boolean) {
        chip.setBackgroundResource(
            if (isSelected) R.drawable.bg_chip_selected else R.drawable.bg_chip_normal
        )
        chip.setTextColor(
            requireContext().getColor(
                if (isSelected) R.color.black_main else android.R.color.darker_gray
            )
        )
    }

    private fun setupRecycler() {
        artworkAdapter = ArtworkAdapter(
            list = MockData.artworks,
            onClick = { artwork -> openDetail(artwork) }
        )
        binding.recyclerExplore.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerExplore.adapter = artworkAdapter

        binding.btnFilter.setOnClickListener {
            val sheet = FilterBottomSheet(currentFilter) { newFilter ->
                currentFilter = newFilter
                refreshChips()
                applyFilters()
            }
            sheet.show(parentFragmentManager, "filter")
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                currentQuery = s.toString().trim()
                applyFilters()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun applyFilters() {
        var result = MockData.artworks

        // Filtro categoría
        if (currentFilter.category != "Todos") {
            result = result.filter { it.category == currentFilter.category }
        }

        // Filtro técnica
        if (currentFilter.technique != "Todos") {
            result = result.filter { it.technique == currentFilter.technique }
        }

        // Filtro precio
        result = when (currentFilter.priceRange) {
            "Hasta S/500"  -> result.filter { it.price <= 500 }
            "S/500-1K"     -> result.filter { it.price in 500.0..1000.0 }
            "S/1K-2K"      -> result.filter { it.price in 1000.0..2000.0 }
            "S/2K+"        -> result.filter { it.price > 2000 }
            else           -> result
        }

        // Búsqueda por texto
        if (currentQuery.isNotEmpty()) {
            result = result.filter {
                it.name.contains(currentQuery, ignoreCase = true) ||
                        it.artist.contains(currentQuery, ignoreCase = true) ||
                        it.category.contains(currentQuery, ignoreCase = true)
            }
        }

        // Actualizar UI
        artworkAdapter.updateList(result)
        binding.txtResultCount.text = "${result.size} obra${if (result.size != 1) "s" else ""} encontrada${if (result.size != 1) "s" else ""}"

        if (result.isEmpty()) {
            binding.recyclerExplore.visibility = View.GONE
            binding.layoutEmpty.visibility = View.VISIBLE
        } else {
            binding.recyclerExplore.visibility = View.VISIBLE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    private fun openDetail(artwork: Artwork) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}