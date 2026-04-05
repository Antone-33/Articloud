package com.articloud.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.articloud.R
import com.articloud.SessionManager
import com.articloud.adapter.ArtworkAdapter
import com.articloud.databinding.FragmentHomeBinding
import com.articloud.model.Artwork
import com.bumptech.glide.Glide
import androidx.core.view.ViewCompat


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
 class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val artworks = listOf(
            Artwork(1, "Cuadro Abstracto", 120.0, "https://i.imgur.com/1.jpg", "Arte moderno"),
            Artwork(2, "Paisaje", 200.0, "https://i.imgur.com/2.jpg", "Naturaleza")
        )

        val adapter = ArtworkAdapter(artworks) { artwork ->
            // aquí luego detalle
        }

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        binding.recyclerView.adapter = adapter
        binding.txtProfile.text = "G" // primera letra del nombre

        // OBRA DESTACADA
        Glide.with(this)
            .load("https://media.gq.com.mx/photos/60c4fa6dfc378adc79f5f139/16:9/w_1920,c_limit/grito-1035007370.jpg")
            .into(binding.imgFeatured)

        // LOGIN
        if (SessionManager.isLogged) {
            binding.btnGuest.visibility = View.GONE
            binding.iconBell.visibility = View.VISIBLE
            binding.txtProfile.visibility = View.VISIBLE
        } else {
            binding.btnGuest.visibility = View.VISIBLE
            binding.iconBell.visibility = View.GONE
            binding.txtProfile.visibility = View.GONE
        }

        binding.txtLogin.setOnClickListener {
            // abrir login activity
        }



        return binding.root


    }
}