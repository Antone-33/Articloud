package com.articloud.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.articloud.ViewModels.DetailViewModel
import com.bumptech.glide.Glide
import com.articloud.model.Obra
import com.articloud.network.RetrofitClient
import com.articloud.data.Repository.ObrasRepository
import com.articloud.databinding.ActivityDetailBinding
import com.articloud.utils.SessionManager

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var session: SessionManager

    private var obra: Obra? = null
    private var cantidad = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        val api = RetrofitClient.create(session)
        val repository = ObrasRepository(api)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(repository) as T
            }
        })[DetailViewModel::class.java]

        val idObra = intent.getIntExtra("id_obra", -1)

        setupObservers()
        setupEvents()

        if (idObra != -1) {
            viewModel.getObrasById(idObra)
        } else {
            showError("ID de obra inválido")
        }
    }

    // 🔹 Observers
    private fun setupObservers() {

        viewModel.loading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.obra.observe(this) {
            obra = it
            bindData(it)
        }

        viewModel.error.observe(this) {
            it?.let { showError(it) }
        }
    }

    // 🔹 Eventos UI
    private fun setupEvents() {

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnMas.setOnClickListener {
            obra?.let {
                if (cantidad < it.stock) {
                    cantidad++
                    actualizarCantidad()
                }
            }
        }

        binding.btnMenos.setOnClickListener {
            if (cantidad > 1) {
                cantidad--
                actualizarCantidad()
            }
        }

        binding.btnAgregarCarrito.setOnClickListener {
            obra?.let {
                if (cantidad > it.stock) {
                    showError("Stock insuficiente")
                    return@setOnClickListener
                }

                // 🔥 Aquí puedes integrar tu carrito
                Log.d("CARRITO", "Agregado: ${it.titulo} x$cantidad")
            }
        }
    }

    // 🔹 Bind de datos
    private fun bindData(obra: Obra) {

        binding.tvTitulo.text = obra.titulo
        binding.tvDescripcion.text = obra.descripcion
        binding.tvPrecio.text = "S/ %.2f".format(obra.precio)
        binding.tvStock.text = "${obra.stock} disponibles"
        binding.tvIdObra.text = "#${obra.idObra}"

        // Imagen
        if (!obra.image_url.isNullOrBlank()) {
            Glide.with(this)
                .load(obra.image_url)
                .centerCrop()
                .into(binding.ivObra)
        }

        actualizarCantidad()
    }

    // 🔹 Cantidad + subtotal
    private fun actualizarCantidad() {
        binding.tvCantidad.text = cantidad.toString()

        obra?.let {
            val subtotal = it.precio * cantidad
            binding.tvSubtotal.text = "S/ %.2f".format(subtotal)
        }
    }

    // 🔹 Mostrar error
    private fun showError(msg: String) {
        binding.tvError.text = msg
        binding.tvError.visibility = View.VISIBLE
    }
}