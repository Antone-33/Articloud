package com.articloud

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.articloud.network.RetrofitClient
import com.articloud.data.Repository.FavoritosRepository
import com.articloud.databinding.ActivityFavoritosBinding
import com.articloud.ui.detail.DetailActivity
import com.articloud.ui.favoritos.FavoritosAdapter
import com.articloud.ui.favoritos.FavoritosViewModel
import com.articloud.utils.SessionManager

class FavoritosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritosBinding
    private lateinit var session: SessionManager
    private lateinit var viewModel: FavoritosViewModel
    private lateinit var adapter: FavoritosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = android.graphics.Color.parseColor("#0D0D0D")

        session = SessionManager(this)

        // Verificar sesión activa
        if (!session.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val api  = RetrofitClient.create(session)
        val repo = FavoritosRepository(api)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return FavoritosViewModel(repo) as T
            }
        })[FavoritosViewModel::class.java]

        setupRecyclerView()
        setupObservers()
        setupListeners()

        // GET /api/favoritos/usuario/{idUsuario}
        viewModel.cargarFavoritos(session.getUserId())
    }

    private fun setupRecyclerView() {
        adapter = FavoritosAdapter(
            onEliminarClick = { favorito ->                // ← id del usuario viene del objeto anidado
                viewModel.eliminar(
                    favorito.usuario.idUsuario,
                    favorito.obra.idObra
                )
            },
            onObraClick = { favorito ->
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("id_obra", favorito.obra.idObra)  // ← favorito.obra
                startActivity(intent)
            }
        )

        binding.recyclerFavoritos.layoutManager = LinearLayoutManager(this)
        binding.recyclerFavoritos.adapter = adapter
    }

    private fun setupObservers() {

        // Lista de favoritos
        viewModel.favoritos.observe(this) { favoritos ->
            adapter.submitList(favoritos)

            val empty = favoritos.isEmpty()

            // ← ID exacto de tu XML
            binding.tvTotalFavoritos.text = "${favoritos.size} favoritos"

            // Mostrar/ocultar estado vacío
            binding.layoutVacio.visibility       = if (empty) View.VISIBLE else View.GONE
            binding.recyclerFavoritos.visibility = if (empty) View.GONE   else View.VISIBLE

            // Ocultar error si cargó bien
            binding.tvError.visibility = View.GONE
        }

        // Error
        viewModel.error.observe(this) { msg ->
            msg?.let {
                binding.tvError.text       = it
                binding.tvError.visibility = View.VISIBLE
            }
        }

        // Mensaje al eliminar
        viewModel.mensaje.observe(this) { msg ->
            msg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        // Loading — tu XML no tiene ProgressBar
        // si quieres agregar uno usa este observer:
        // viewModel.loading.observe(this) { loading -> }
    }

    private fun setupListeners() {
        // btnBack es TextView en tu XML — no ImageButton
        binding.btnBack.setOnClickListener { finish() }
    }
}