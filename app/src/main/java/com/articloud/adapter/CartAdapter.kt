package com.articloud.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.articloud.databinding.ItemCartBinding
import com.articloud.model.CartManager.CartItem
import com.bumptech.glide.Glide

class CartAdapter(
    private var items: List<CartItem>,
    private val onIncrease: (CartItem) -> Unit,
    private val onDecrease: (CartItem) -> Unit,
    private val onRemove: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        Glide.with(holder.itemView.context)
            .load(item.artwork.image)
            .centerCrop()
            .placeholder(android.R.color.darker_gray)
            .into(holder.binding.imgCart)

        holder.binding.txtCartName.text = item.artwork.name
        holder.binding.txtCartArtist.text = item.artwork.artist
        holder.binding.txtCartPrice.text = "S/ ${String.format("%,.0f", item.artwork.price)}"
        holder.binding.txtQuantity.text = item.quantity.toString()

        holder.binding.btnIncrease.setOnClickListener { onIncrease(item) }
        holder.binding.btnDecrease.setOnClickListener { onDecrease(item) }
        holder.binding.btnRemove.setOnClickListener { onRemove(item) }
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: List<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}