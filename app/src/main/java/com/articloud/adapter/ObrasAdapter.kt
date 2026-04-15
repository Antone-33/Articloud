package com.articloud.ui.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.articloud.model.Obra
import com.articloud.databinding.ItemObraBinding
import com.articloud.model.Categoria
import com.articloud.ui.cart.CarritoAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
class ObrasAdapter(
        private val onObraClick: (Obra) -> Unit,
        private val onAgregarClick: (Obra) -> Unit,
        private val onFavoritoClick: (Obra) -> Unit,
        private val esFavorito: (Int) -> Boolean,
        private var categorias: List<Categoria> = emptyList()
        ) : ListAdapter<Obra, ObrasAdapter.ObraVH>(DiffCallback) {

    fun setCategorias(lista: List<Categoria>) {
        categorias = lista
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObraVH {
        val binding = ItemObraBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ObraVH(binding)
    }

    override fun onBindViewHolder(holder: ObraVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ObraVH(private val b: ItemObraBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(obra: Obra) {

            b.tvTitulo.text = obra.titulo
            b.tvPrecio.text = "S/ %.2f".format(obra.precio)

            // ❤️ Estado REAL desde ViewModel
            val fav = esFavorito(obra.idObra)

            b.btnFavorito.setImageResource(
                if (fav)
                    android.R.drawable.btn_star_big_on
                else
                    android.R.drawable.btn_star_big_off
            )

            b.btnFavorito.setOnClickListener {
                onFavoritoClick(obra)
            }

            b.root.setOnClickListener { onObraClick(obra) }
            b.btnAgregar.setOnClickListener { onAgregarClick(obra) }
        }
        // Colores de fondo cuando no hay imagen_url
        private fun colorPorIndice(index: Int): Int {
            val colores = listOf(
                0xFF8B4513.toInt(),  // café
                0xFF1a3a5c.toInt(),  // azul oscuro
                0xFF2d5016.toInt(),  // verde
                0xFF4a1942.toInt(),  // morado
                0xFF1c1c2e.toInt(),  // azul noche
                0xFF7c3a00.toInt(),  // naranja oscuro
            )
            return colores[index % colores.size]
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Obra>() {
        override fun areItemsTheSame(a: Obra, b: Obra) =
            a.idObra == b.idObra

        override fun areContentsTheSame(a: Obra, b: Obra) = a == b
    }
}
