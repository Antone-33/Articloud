package com.articloud.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.articloud.databinding.ItemCheckoutSummaryBinding
import com.articloud.model.CartManager.CartItem
import com.bumptech.glide.Glide

class CheckoutSummaryAdapter(
    private val items: List<CartItem>
) : RecyclerView.Adapter<CheckoutSummaryAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCheckoutSummaryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCheckoutSummaryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView.context)
            .load(item.artwork.image)
            .centerCrop()
            .into(holder.binding.imgSummary)
        holder.binding.txtSummaryName.text = item.artwork.name
        holder.binding.txtSummaryQty.text = "x${item.quantity}"
        val total = item.artwork.price * item.quantity
        holder.binding.txtSummaryPrice.text = "S/ ${String.format("%,.0f", total)}"
    }

    override fun getItemCount() = items.size
}