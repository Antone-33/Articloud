package com.articloud

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.articloud.ViewModels.PedidosViewModels
import com.articloud.network.RetrofitClient
import com.articloud.data.repository.PedidosRepository
import com.articloud.databinding.ActivityOrdersBinding
import com.articloud.adapter.PedidosAdapter
import com.articloud.ui.catalog.CatalogActivity
import com.articloud.utils.SessionManager

class OrdersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrdersBinding
    private lateinit var session: SessionManager
    private lateinit var viewModel: PedidosViewModels
    private lateinit var adapter: PedidosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = android.graphics.Color.parseColor("#0D0D0D")

        session = SessionManager(this)

        // Verificar sesión
        if (!session.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val api  = RetrofitClient.create(session)
        val repo = PedidosRepository(api)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PedidosViewModels(repo) as T
            }
        })[PedidosViewModels::class.java]

        setupRecyclerView()
        setupObservers()
        setupListeners()

        // GET /api/pedidos/buscar/usuario/{idUsuario}
        viewModel.cargarPedidos(session.getUserId())
    }

    private fun setupRecyclerView() {
        adapter = PedidosAdapter(
            onVerDetalleClick = { pedido ->
                // Por ahora solo muestra el id
                // Puedes crear un PedidoDetailActivity si necesitas más detalle
                Toast.makeText(
                    this,
                    "Pedido N° ${pedido.idPedido} — ${pedido.estado}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        binding.recyclerPedidos.layoutManager = LinearLayoutManager(this)
        binding.recyclerPedidos.adapter = adapter
    }

    private fun setupObservers() {

        // Loading
        viewModel.loading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        // Lista de pedidos
        viewModel.pedidos.observe(this) { pedidos ->
            adapter.submitList(pedidos)

            val empty = pedidos.isEmpty()
            binding.layoutVacio.visibility    = if (empty) View.VISIBLE else View.GONE
            binding.recyclerPedidos.visibility = if (empty) View.GONE else View.VISIBLE
            binding.tvTotal.text = "${pedidos.size} pedidos realizados"
        }

        // Error
        viewModel.error.observe(this) { msg ->
            msg?.let {
                binding.tvError.text       = it
                binding.tvError.visibility = View.VISIBLE
            }
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }

        // Botón del estado vacío
        binding.btnIrCatalogo.setOnClickListener {
            startActivity(Intent(this, CatalogActivity::class.java))
            finish()
        }
    }
}