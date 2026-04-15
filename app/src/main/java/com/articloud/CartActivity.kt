package com.articloud.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.articloud.LoginActivity
import com.articloud.data.cart.CarritoManager
import com.articloud.databinding.ActivityCartBinding
import com.articloud.model.CarritoItem
import com.articloud.model.PedidoDetalleRequest
import com.articloud.model.PedidoRequest
import com.articloud.network.RetrofitClient
import com.articloud.ui.checkout.ResumenActivity
import com.articloud.utils.SessionManager
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: CarritoAdapter
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        setupRecyclerView()
        setupObservers()
        setupEvents()
    }

    // 🔹 RecyclerView
    private fun setupRecyclerView() {

        adapter = CarritoAdapter(
            onCantidadChange = { idObra, cantidad ->
                CarritoManager.actualizarCantidad(idObra, cantidad)
            },
            onEliminar = { item ->
                CarritoManager.eliminarItem(item.obra.idObra)
            }
        )

        binding.recyclerCarrito.apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
            adapter = this@CartActivity.adapter
        }
    }

    // 🔹 OBSERVAR cambios del carrito (CLAVE)
    private fun setupObservers() {

        CarritoManager.items.observe(this) { lista ->

            adapter.submitList(lista)
            calcularTotal(lista)
            mostrarEstadoVacio(lista)
        }
    }

    // 🔹 Eventos
    private fun setupEvents() {

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnComprar.setOnClickListener {

            val items = CarritoManager.getItemsSnapshot()

            if (items.isEmpty()) return@setOnClickListener

            if (!session.isLoggedIn()) {
                startActivity(Intent(this, LoginActivity::class.java))
                return@setOnClickListener
            }
            startActivity(Intent(this, ResumenActivity::class.java))
        }
    }

    private fun crearPedido(items: List<CarritoItem>) {
        val api = RetrofitClient.create(session)
        val total = items.sumOf { it.subtotal }

        binding.btnComprar.isEnabled = false
        binding.btnComprar.text = "Procesando"

        lifecycleScope.launch {
            try {
                val body = PedidoRequest(
                    idUsuario = session.getUserId(),
                    total = total,
                    detalle = items.map { items -> PedidoDetalleRequest( idObra = items.obra.idObra,
                        cantidad= items.cantidad, precio = items.obra.precio)
                    }
                )
                val r = api.crearPedido(body)

                if (r.isSuccessful) {
                    limpiarCarrito()
                    mostrarConfirmacion(r.body()?.idPedido)
                } else {
                    Toast.makeText(
                        this@CartActivity,
                        "Error al procesar el pedido",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@CartActivity,
                    "Sin conexion al servidor",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                binding.btnComprar.isEnabled = true
                binding.btnComprar.text = "Finalizar compra"
            }
        }
    }

        private fun limpiarCarrito() {
            CarritoManager.limpiar()
        }

        private fun mostrarConfirmacion(idPedido: Int?) {
            Toast.makeText(
                this, "!Pedido N° $idPedido confirmado!",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }

        // 🔹 Calcular total
        private fun calcularTotal(lista: List<CarritoItem>) {
            val total = lista.sumOf { it.subtotal }
            binding.tvTotal.text = "S/ %.2f".format(total)
        }

        // 🔹 Estado vacío
        private fun mostrarEstadoVacio(lista: List<CarritoItem>) {
            if (lista.isEmpty()) {
                binding.layoutVacio.visibility = View.VISIBLE
                binding.recyclerCarrito.visibility = View.GONE
            } else {
                binding.layoutVacio.visibility = View.GONE
                binding.recyclerCarrito.visibility = View.VISIBLE
            }
        }
}