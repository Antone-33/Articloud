package com.articloud.ui.checkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.articloud.databinding.ItemResumenBinding
import com.articloud.model.CarritoItem
import com.bumptech.glide.Glide

class ResumenAdapter(
    private var items: List<CarritoItem> = emptyList()
) : RecyclerView.Adapter<ResumenAdapter.ResumenVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResumenVH {
        val binding = ItemResumenBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ResumenVH(binding)
    }

    override fun onBindViewHolder(holder: ResumenVH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(lista: List<CarritoItem>) {
        items = lista
        notifyDataSetChanged()
    }

    inner class ResumenVH(private val b: ItemResumenBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(item: CarritoItem) {

            val obra = item.obra

            // 📌 Datos
            b.tvTitulo.text = obra.titulo
            b.tvCantidad.text = "x${item.cantidad}"
            b.tvPrecio.text = "S/ %.2f".format(obra.precio)
            b.tvSubtotal.text = "S/ %.2f".format(item.subtotal)

            // 🖼 Imagen
            if (!obra.image_url.isNullOrEmpty()) {
                Glide.with(b.root.context)
                    .load(obra.image_url)
                    .centerCrop()
                    .into(b.ivObra)
            } else {
                b.ivObra.setImageResource(android.R.color.darker_gray)
            }
        }
    }
}