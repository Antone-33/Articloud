package com.articloud.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.articloud.databinding.ItemArtworkBinding
import com.articloud.model.Artwork
import com.bumptech.glide.Glide

class ArtworkAdapter(
    private val list: List<Artwork>,
    private val onClick: (Artwork) -> Unit
) : RecyclerView.Adapter<ArtworkAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemArtworkBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArtworkBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.binding.txtName.text = item.name
        holder.binding.txtPrice.text = "$${item.price}"
        Glide.with(holder.itemView.context)
            .load(item.image)
            .into(holder.binding.imgArtwork)

        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount() = list.size
}