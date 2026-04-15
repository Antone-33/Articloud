package com.articloud.ui.checkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.articloud.data.cart.CarritoManager
import com.articloud.data.repository.PedidosRepository
import com.articloud.model.CarritoItem
import com.articloud.network.RetrofitClient
import com.articloud.databinding.ActivityResumenBinding
import com.articloud.ui.catalog.CatalogActivity
import com.articloud.utils.SessionManager
import kotlinx.coroutines.launch

class ResumenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResumenBinding
    private lateinit var session: SessionManager
    private lateinit var adapter: ResumenAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = SessionManager(this)

        setupRecycler()
        setupObservers()
        setupActions()
    }
    private fun setupRecycler() {
        adapter = ResumenAdapter()
        binding.recyclerResumen.layoutManager = LinearLayoutManager(this)
        binding.recyclerResumen.adapter = adapter
    }

    private fun setupObservers() {
        CarritoManager.items.observe(this) { lista ->
            adapter.submitList(lista)
            pintarResumen(lista)
        }
    }
    private fun pintarResumen(lista: List<CarritoItem>) {
        val items = lista.sumOf { it.cantidad }
        val subtotal = lista.sumOf { it.subtotal }
        binding.tvItems.text = "$items items"
        binding.tvSubtotal.text = "S/ %.2f".format(subtotal)
        binding.tvTotal.text = "S/ %.2f".format(subtotal)
    }

    private fun setupActions() {
        binding.btnConfirmar.setOnClickListener { enviarPedido() }
        binding.btnBack.setOnClickListener { finish() }
        binding.btnBackBottom.setOnClickListener { finish() }
    }
    private fun enviarPedido() {

        val items = CarritoManager.getItemsSnapshot()

        if (items.isEmpty()) {
            Toast.makeText(this, "Carrito vacío", Toast.LENGTH_SHORT).show()
            return }

        binding.progressBar.visibility = View.VISIBLE
        binding.btnConfirmar.isEnabled = false

        val api = RetrofitClient.create(session)
        val repository = PedidosRepository(api)

        lifecycleScope.launch {

            val result = repository.crearPedido(
                idUsuario = session.getUserId(),
                items = items
            )

            binding.progressBar.visibility = View.GONE
            binding.btnConfirmar.isEnabled = true

            result.fold(
                onSuccess = {
                    Toast.makeText(this@ResumenActivity, "Pedido realizado", Toast.LENGTH_SHORT).show()

                    // 🔥 Uso de tu nueva función de CarritoManager
                    CarritoManager.limpiar()

                    val intent = Intent(this@ResumenActivity, CatalogActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                },
                onFailure = { e ->
                    Toast.makeText(this@ResumenActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    // =========================================
    // 🔹 Simulación carrito
    // =========================================
    private fun limpiarCarrito(){
        CarritoManager.limpiar()
    }

}




