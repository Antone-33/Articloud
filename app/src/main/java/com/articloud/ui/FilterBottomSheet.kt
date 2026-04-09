package com.articloud.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.articloud.databinding.FragmentFilterBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

data class FilterState(
    val category: String = "Todos",
    val technique: String = "Todos",
    val priceRange: String = "Todos"
)

class FilterBottomSheet(
    private val currentFilter: FilterState,
    private val onApply: (FilterState) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: FragmentFilterBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var selectedCategory = currentFilter.category
    private var selectedTechnique = currentFilter.technique
    private var selectedPrice = currentFilter.priceRange

    private val categories  = listOf("Todos", "Abstracto", "Paisaje", "Figurativo", "Urbano", "Retrato")
    private val techniques  = listOf("Todos", "Óleo", "Acrílico", "Acuarela", "Mixta", "Pastel")
    private val priceRanges = listOf("Todos", "Hasta S/500", "S/500-1K", "S/1K-2K", "S/2K+")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buildChips(binding.chipGroupCategory, categories, selectedCategory) { selectedCategory = it }
        buildChips(binding.chipGroupTechnique, techniques, selectedTechnique) { selectedTechnique = it }
        buildChips(binding.chipGroupPrice, priceRanges, selectedPrice) { selectedPrice = it }

        binding.btnCloseFilter.setOnClickListener { dismiss() }

        binding.btnClearFilter.setOnClickListener {
            selectedCategory = "Todos"
            selectedTechnique = "Todos"
            selectedPrice = "Todos"
            // Rebuild chips
            binding.chipGroupCategory.removeAllViews()
            binding.chipGroupTechnique.removeAllViews()
            binding.chipGroupPrice.removeAllViews()
            buildChips(binding.chipGroupCategory, categories, selectedCategory) { selectedCategory = it }
            buildChips(binding.chipGroupTechnique, techniques, selectedTechnique) { selectedTechnique = it }
            buildChips(binding.chipGroupPrice, priceRanges, selectedPrice) { selectedPrice = it }
        }

        binding.btnApplyFilter.setOnClickListener {
            onApply(FilterState(selectedCategory, selectedTechnique, selectedPrice))
            dismiss()
        }
    }

    private fun buildChips(
        container: ViewGroup,
        items: List<String>,
        selected: String,
        onSelect: (String) -> Unit
    ) {
        container.removeAllViews()
        items.forEach { label ->
            val chip = LayoutInflater.from(requireContext())
                .inflate(com.articloud.R.layout.item_chip, container, false) as TextView
            chip.text = label
            updateChipStyle(chip, label == selected)
            chip.setOnClickListener {
                onSelect(label)
                for (i in 0 until container.childCount) {
                    val c = container.getChildAt(i) as TextView
                    updateChipStyle(c, c.text == label)
                }
            }
            container.addView(chip)
        }
    }

    private fun updateChipStyle(chip: TextView, isSelected: Boolean) {
        chip.setBackgroundResource(
            if (isSelected) com.articloud.R.drawable.bg_chip_selected
            else com.articloud.R.drawable.bg_chip_normal
        )
        chip.setTextColor(
            requireContext().getColor(
                if (isSelected) com.articloud.R.color.black_main
                else android.R.color.darker_gray
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}