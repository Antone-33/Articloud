package com.articloud.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.articloud.databinding.ItemPedidoBinding
import com.articloud.model.EstadoPedido
import com.articloud.model.Pedido

class PedidosAdapter(
    private val onVerDetalleClick: (Pedido) -> Unit
) : ListAdapter<Pedido, PedidosAdapter.PedidoVH>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PedidoVH(
            ItemPedidoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: PedidoVH, position: Int) =
        holder.bind(getItem(position))

    inner class PedidoVH(private val b: ItemPedidoBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(pedido: Pedido) {

            // N° pedido
            b.tvNumeroPedido.text = "Pedido N° ${pedido.idPedido}"

            // Total
            b.tvTotal.text = "S/ ${String.format("%.2f", pedido.total)}"

            // Fecha — formato legible
            b.tvFecha.text = formatearFecha(pedido.fechaRegistro)

            // Estado + color del badge
            b.tvEstado.text = pedido.estado.name
            b.tvEstado.setBackgroundColor(colorEstado(pedido.estado))

            // Click en ver detalle
            b.btnVerDetalle.setOnClickListener { onVerDetalleClick(pedido) }
            b.root.setOnClickListener { onVerDetalleClick(pedido) }
        }

        // Color del badge según estado
        private fun colorEstado(estado: EstadoPedido): Int {
            return when (estado) {
                EstadoPedido.PAGADO -> Color.parseColor("#43A047")  // verde
                EstadoPedido.CANCELADO -> Color.parseColor("#E53935")  // rojo
                else         -> Color.parseColor("#FB8C00")  // naranja PENDIENTE
            }
        }

        // Formatear fecha "2026-04-12T00:00:00" → "12 abril 2026"
        private fun formatearFecha(fecha: String): String {
            return try {
                val partes = fecha.substring(0, 10).split("-")
                val meses  = listOf(
                    "", "enero","febrero","marzo","abril","mayo","junio",
                    "julio","agosto","septiembre","octubre","noviembre","diciembre"
                )
                "${partes[2]} ${meses[partes[1].toInt()]} ${partes[0]}"
            } catch (e: Exception) {
                fecha
            }
        }
    }

    companion object DIFF_CALLBACK : DiffUtil.ItemCallback<Pedido>() {
        override fun areItemsTheSame(a: Pedido, b: Pedido) =
            a.idPedido == b.idPedido
        override fun areContentsTheSame(a: Pedido, b: Pedido) = a == b
    }
}