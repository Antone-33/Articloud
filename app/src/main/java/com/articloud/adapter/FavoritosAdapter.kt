package com.articloud.ui.favoritos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.articloud.model.Favorito
import com.articloud.databinding.ItemFavoritosBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class FavoritosAdapter(
    private val onEliminarClick : (Favorito) -> Unit,
    private val onObraClick     : (Favorito) -> Unit
) : ListAdapter<Favorito, FavoritosAdapter.FavoritoVH>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FavoritoVH(
            ItemFavoritosBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: FavoritoVH, position: Int) =
        holder.bind(getItem(position))

    inner class FavoritoVH(private val b: ItemFavoritosBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(favorito: Favorito) {

            // ← ahora accedes por favorito.obra
            b.tvTitulo.text = favorito.obra.titulo
            b.tvPrecio.text = "S/ ${String.format("%.2f", favorito.obra.precio)}"

            // Imagen con Glide
            if (!favorito.obra.image_url.isNullOrEmpty()) {
                Glide.with(b.root.context)
                    .load(favorito.obra.image_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .placeholder(android.R.color.darker_gray)
                    .into(b.ivObra)
            } else {
                val colores = listOf(
                    0xFF8B4513.toInt(),
                    0xFF1a3a5c.toInt(),
                    0xFF2d5016.toInt(),
                    0xFF4a1942.toInt()
                )
                b.ivObra.setBackgroundColor(colores[adapterPosition % colores.size])
                b.ivObra.setImageDrawable(null)
            }

            // Click card → detalle de la obra
            b.root.setOnClickListener { onObraClick(favorito) }

            // Click eliminar
            b.btnEliminar.setOnClickListener { onEliminarClick(favorito) }
        }
    }
    companion object DiffCallback : DiffUtil.ItemCallback<Favorito>() {
        override fun areItemsTheSame(a: Favorito, b: Favorito) =
            a.idFavorito == b.idFavorito
        override fun areContentsTheSame(a: Favorito, b: Favorito) = a == b
    }
}