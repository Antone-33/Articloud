package com.articloud.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.articloud.model.CarritoItem
import com.articloud.databinding.ItemCarritoBinding
import com.articloud.model.Obra

class CarritoAdapter(
    private val onCantidadChange: (Int, Int) -> Unit,
    private val onEliminar: (CarritoItem) -> Unit
) : ListAdapter<CarritoItem, CarritoAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(val binding: ItemCarritoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CarritoItem) {

            binding.tvTitulo.text = item.obra.titulo
            binding.tvPrecio.text = "S/ %.2f".format(item.obra.precio)
            binding.tvCantidad.text = item.cantidad.toString()
            binding.tvSubtotal.text = "S/ %.2f".format(item.subtotal)

            // Imagen
            Glide.with(binding.root)
                .load(item.obra.image_url)
                .centerCrop()
                .into(binding.ivObra)

            // ➕ Aumentar cantidad
            binding.btnMas.setOnClickListener {
                onCantidadChange(item.obra.idObra, item.cantidad + 1)
            }

            // ➖ Disminuir cantidad
            binding.btnMenos.setOnClickListener {
                if (item.cantidad > 1) {
                    onCantidadChange(item.obra.idObra, item.cantidad - 1)
                }
            }

            // 🗑️ Eliminar
            binding.btnEliminar.setOnClickListener {
                onEliminar(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCarritoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // 🔹 DiffUtil
    class DiffCallback : DiffUtil.ItemCallback<CarritoItem>() {
        override fun areItemsTheSame(oldItem: CarritoItem, newItem: CarritoItem): Boolean {
            return oldItem.obra.idObra == newItem.obra.idObra
        }

        override fun areContentsTheSame(oldItem: CarritoItem, newItem: CarritoItem): Boolean {
            return oldItem == newItem
        }
    }
}