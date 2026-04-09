package com.articloud.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.articloud.databinding.ItemArtworkBinding
import com.articloud.model.Artwork
import com.bumptech.glide.Glide

class ArtworkAdapter(
    private var list: List<Artwork>,
    private val onFavoriteClick: ((Artwork) -> Unit)? = null,
    private val onClick: (Artwork) -> Unit
) : RecyclerView.Adapter<ArtworkAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemArtworkBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArtworkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val ctx = holder.itemView.context

        // Imagen
        Glide.with(ctx)
            .load(item.image)
            .centerCrop()
            .placeholder(android.R.color.darker_gray)
            .into(holder.binding.imgArtwork)

        // Badge
        if (item.badge != null) {
            holder.binding.txtBadge.visibility = View.VISIBLE
            holder.binding.txtBadge.text = item.badge
            // Color del badge según tipo
            val badgeColor = when (item.badge) {
                "Premium" -> "#1A1A1A"
                "Oferta" -> "#8B0000"
                else -> "#C8A96A"
            }
            val textColor = when (item.badge) {
                "Premium" -> "#C8A96A"
                "Oferta" -> "#FFFFFF"
                else -> "#000000"
            }
            holder.binding.txtBadge.setBackgroundColor(android.graphics.Color.parseColor(badgeColor))
            holder.binding.txtBadge.setTextColor(android.graphics.Color.parseColor(textColor))
        } else {
            holder.binding.txtBadge.visibility = View.GONE
        }

        // Info
        holder.binding.txtCategorySize.text = "${item.category} · ${item.size}"
        holder.binding.txtName.text = item.name
        holder.binding.txtArtist.text = "por ${item.artist}"
        holder.binding.txtRating.text = item.rating.toString()
        holder.binding.txtReviews.text = "(${item.reviewCount})"
        holder.binding.txtPrice.text = "S/ ${String.format("%,.0f", item.price)}"

        // Precio original tachado
        if (item.originalPrice != null) {
            holder.binding.txtOriginalPrice.visibility = View.VISIBLE
            holder.binding.txtOriginalPrice.text = "S/ ${String.format("%,.0f", item.originalPrice)}"
            holder.binding.txtOriginalPrice.paintFlags =
                holder.binding.txtOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.binding.txtOriginalPrice.visibility = View.GONE
        }

        // Favorito — sincronizado con FavoritesManager
        val isFav = com.articloud.model.FavoritesManager.isFavorite(item.id)
        holder.binding.btnFavorite.text = if (isFav) "♥" else "♡"
        holder.binding.btnFavorite.setTextColor(
            ctx.getColor(if (isFav) com.articloud.R.color.gold else android.R.color.white)
        )
        holder.binding.btnFavorite.setOnClickListener {
            com.articloud.model.FavoritesManager.toggle(item)
            notifyItemChanged(position)
            onFavoriteClick?.invoke(item)
        }

        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<Artwork>) {
        list = newList
        notifyDataSetChanged()
    }
}