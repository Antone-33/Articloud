package com.articloud.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.articloud.databinding.ItemCategoryBinding
import com.articloud.model.Category

class CategoryAdapter(
    private val list: List<Category>,
    private val onClick: (Category, Int) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var selectedPosition = 0

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val isSelected = position == selectedPosition

        holder.binding.txtCategoryIcon.text = item.icon
        holder.binding.txtCategoryName.text = item.name

        // Cambiar estilo según selección
        holder.binding.txtCategoryIcon.setBackgroundResource(
            if (isSelected) com.articloud.R.drawable.bg_category_selected
            else com.articloud.R.drawable.bg_category_normal
        )
        holder.binding.txtCategoryIcon.setTextColor(
            holder.itemView.context.getColor(
                if (isSelected) com.articloud.R.color.black_main
                else android.R.color.white
            )
        )
        holder.binding.txtCategoryName.setTextColor(
            holder.itemView.context.getColor(
                if (isSelected) com.articloud.R.color.gold
                else android.R.color.darker_gray
            )
        )

        holder.itemView.setOnClickListener {
            val prev = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(prev)
            notifyItemChanged(selectedPosition)
            onClick(item, selectedPosition)
        }
    }

    override fun getItemCount() = list.size
}