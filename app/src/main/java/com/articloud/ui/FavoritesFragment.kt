package com.articloud.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.articloud.LoginActivity
import com.articloud.SessionManager
import com.articloud.databinding.FragmentEmptyStateBinding

class FavoritesFragment : Fragment() {

    private var _binding: FragmentEmptyStateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEmptyStateBinding.inflate(inflater, container, false)

        if (!SessionManager.isLogged(requireContext())) {

            binding.icon.text = "❤"
            binding.title.text = "Guarda tus favoritos"
            binding.desc.text = "Inicia sesión para guardar obras y acceder a tu lista"

            binding.btnLogin.setOnClickListener {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }

        } else {
            binding.title.text = "Aquí irán tus favoritos 🔥"
        }

        return binding.root
    }
}