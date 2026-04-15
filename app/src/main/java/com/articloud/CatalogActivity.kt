package com.articloud.ui.catalog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.articloud.network.RetrofitClient
import com.articloud.data.Repository.ObrasRepository
import com.articloud.databinding.ActivityCatalogBinding
import com.articloud.OrdersActivity
import com.articloud.ViewModels.CatalogViewModel
import com.articloud.adapter.CategoryAdapter
import com.articloud.data.cart.CarritoManager
import com.articloud.model.Obra
import com.articloud.ui.cart.CartActivity
import com.articloud.ui.detail.DetailActivity
import com.articloud.utils.SessionManager

class CatalogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogBinding
    private lateinit var session: SessionManager
    private lateinit var viewModel: CatalogViewModel
    private lateinit var adapter: ObrasAdapter

    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔐 Session
        session = SessionManager(this)

        // 🌐 API + Repository
        val api = RetrofitClient.create(session)
        val repository = ObrasRepository(api)

        // 🧠 ViewModel con Factory
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CatalogViewModel(repository) as T
            }
        })[CatalogViewModel::class.java]

        setupRecyclerView()
        setupObservers()
        setupSearch()
        setupNavBar()

        // 🚀 Llamadas iniciales
        viewModel.loadObras()
        viewModel.loadCategorias()

        viewModel.loadFavoritos(session.getUserId())
        // 👤 Usuario
        if (session.isLoggedIn()) {
            binding.tvSaludo.text = "Hola, ${session.getUserName()}"
        }
    }

    // 🔹 RecyclerView
    private fun setupRecyclerView() {
        adapter = ObrasAdapter(
        onObraClick = { obra -> goToDetail(obra) },
        onAgregarClick = {obra -> CarritoManager.agregarItem(obra)
            Toast.makeText(this, "Agregado: ${obra.titulo}", Toast.LENGTH_SHORT)},
        onFavoritoClick = { obra -> viewModel.toggleFavorito(session.getUserId(), obra)},
            esFavorito = { idObra -> viewModel.estaEnFavoritos(idObra) })

        binding.recyclerObras.apply {
            layoutManager = GridLayoutManager(this@CatalogActivity, 2)
            adapter = this@CatalogActivity.adapter
        }

    }
    private fun setupCategoryRecyclerView() {
        categoryAdapter = CategoryAdapter(emptyList()) { categoria, posicion ->
            // Lógica de filtrado
            if (posicion == 0) {
                viewModel.loadObras() // "Todas"
            } else {
                viewModel.filtrarPorCategoria(categoria.idCategoria)
            }
        }

        binding.recyclerCategories.apply {
            layoutManager =
                LinearLayoutManager(this@CatalogActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }
    }

    // 🔹 Observers
    private fun setupObservers() {
        viewModel.loading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.swipeRefresh.isRefreshing = loading
        }

        viewModel.obras.observe(this) { obras ->
            Log.d("DATA", "Obras recibidas: ${obras.size}")

            adapter.submitList(obras)

            binding.tvTotal.text = "${obras.size} obras"

            // Estado vacío
            binding.layoutVacio.visibility =
                if (obras.isEmpty()) View.VISIBLE else View.GONE

            // Ocultar error si carga bien
            binding.tvError.visibility = View.GONE
        }

        viewModel.categorias.observe(this) { categorias ->
            adapter.setCategorias(categorias)
        }

        viewModel.error.observe(this) { msg ->
            msg?.let {
                Log.e("API", it)
                binding.tvError.text = it
                binding.tvError.visibility = View.VISIBLE
            }
        }

        viewModel.favoritosIds.observe(this){
            adapter.notifyDataSetChanged()
        }

        viewModel.categorias.observe(this) { lista ->
            categoryAdapter.updateData(lista)
        }

    }

    // 🔹 Búsqueda
    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(q: String?) = false

            override fun onQueryTextChange(q: String?): Boolean {
                if (q.isNullOrBlank()) {
                    viewModel.loadObras()
                } else {
                    viewModel.buscar(q)
                }
                return true
            }
        })

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadObras()
        }
    }

    // 🔹 Navegación
    private fun setupNavBar() {

        binding.btnCarrito.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        binding.btnPedidos.setOnClickListener {
            if (session.isLoggedIn()) {
                startActivity(Intent(this, OrdersActivity::class.java))
            } else {
                startActivity(Intent(this, com.articloud.LoginActivity::class.java))
            }
        }



        binding.btnLogout.setOnClickListener {
            session.clearSession()
            startActivity(Intent(this, com.articloud.LoginActivity::class.java))
            finishAffinity()
        }
    }


    // 🔹 Navegar a detalle
    private fun goToDetail(obra: Obra) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("idObra", obra.idObra)
        startActivity(intent)
    }
}