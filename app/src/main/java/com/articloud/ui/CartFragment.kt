package com.articloud.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.articloud.LoginActivity
import com.articloud.R
import com.articloud.SessionManager
import com.articloud.adapter.CartAdapter
import com.articloud.databinding.FragmentCartBinding
import com.articloud.model.CartManager

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartAdapter: CartAdapter

    private val cartListener = {
        refreshCart()
        updateBadge()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        setupRecycler()
        setupClicks()
        CartManager.addListener(cartListener)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        refreshCart()
    }

    private fun setupRecycler() {
        cartAdapter = CartAdapter(
            items = CartManager.getItems(),
            onIncrease = { item ->
                CartManager.increaseQuantity(item.artwork.id)
            },
            onDecrease = { item ->
                CartManager.decreaseQuantity(item.artwork.id)
            },
            onRemove = { item ->
                CartManager.removeItem(item.artwork.id)
                Toast.makeText(requireContext(), "Obra eliminada del carrito", Toast.LENGTH_SHORT).show()
            }
        )
        binding.recyclerCart.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerCart.adapter = cartAdapter
    }

    private fun setupClicks() {
        binding.btnLoginCart.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
        binding.btnExploreGuest.setOnClickListener {
            // Navegar a Explorar
            (requireActivity() as? com.articloud.MainActivity)?.let {
                it.binding.bottomNav.selectedItemId = R.id.nav_explore
            }
        }
        binding.btnExploreEmpty.setOnClickListener {
            (requireActivity() as? com.articloud.MainActivity)?.let {
                it.binding.bottomNav.selectedItemId = R.id.nav_explore
            }
        }
        binding.btnCheckout.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), com.articloud.CheckoutActivity::class.java))
        }
    }

    private fun refreshCart() {
        if (_binding == null) return

        val isLogged = SessionManager.isLogged(requireContext())
        val items = CartManager.getItems()

        when {
            !isLogged -> {
                binding.layoutGuest.visibility = View.VISIBLE
                binding.layoutEmpty.visibility = View.GONE
                binding.layoutContent.visibility = View.GONE
            }
            items.isEmpty() -> {
                binding.layoutGuest.visibility = View.GONE
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.layoutContent.visibility = View.GONE
            }
            else -> {
                binding.layoutGuest.visibility = View.GONE
                binding.layoutEmpty.visibility = View.GONE
                binding.layoutContent.visibility = View.VISIBLE

                cartAdapter.updateItems(items)

                val count = CartManager.getTotalCount()
                binding.txtItemCount.text = "$count obra${if (count != 1) "s" else ""}"

                val subtotal = CartManager.getSubtotal()
                val freeShipping = CartManager.hasFreeShipping()

                binding.txtSubtotal.text = "S/ ${String.format("%,.0f", subtotal)}"
                binding.txtShipping.text = if (freeShipping) "Gratis" else "A calcular"
                binding.txtTotal.text = "S/ ${String.format("%,.0f", subtotal)}"
                binding.btnCheckout.text = "Proceder al pago · S/ ${String.format("%,.0f", subtotal)}"
                binding.layoutFreeShipping.visibility = if (freeShipping) View.VISIBLE else View.GONE
            }
        }
    }

    private fun updateBadge() {
        val activity = requireActivity() as? com.articloud.MainActivity ?: return
        val count = CartManager.getTotalCount()
        val badge = activity.binding.bottomNav.getOrCreateBadge(R.id.nav_cart)
        if (count > 0) {
            badge.isVisible = true
            badge.number = count
            badge.backgroundColor = requireContext().getColor(R.color.gold)
            badge.badgeTextColor = requireContext().getColor(R.color.black_main)
        } else {
            badge.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        CartManager.removeListener(cartListener)
        _binding = null
    }
}