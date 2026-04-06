package com.articloud.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.articloud.LoginActivity
import com.articloud.SessionManager
import com.articloud.databinding.FragmentCartBinding

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCartBinding.inflate(inflater, container, false)

        binding.btnLoginCart.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        updateUI()

        return binding.root
    }

    private fun updateUI() {
        if (SessionManager.isLogged(requireContext())) {
            binding.layoutGuest.visibility = View.GONE
            binding.layoutContent.visibility = View.VISIBLE
        } else {
            binding.layoutGuest.visibility = View.VISIBLE
            binding.layoutContent.visibility = View.GONE
        }
    }
}