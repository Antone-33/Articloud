package com.articloud.model
data class Artwork(
    val id: Int,
    val name: String,
    val artist: String,
    val price: Double,
    val originalPrice: Double?,
    val image: String,
    val description: String,
    val category: String,
    val size: String,
    val technique: String,
    val year: Int,
    val rating: Float,
    val reviewCount: Int,
    val badge: String?,        // "Más vendido", "Nuevo", "Premium", "Oferta", null
    val isFeatured: Boolean,
    val hasFreeShipping: Boolean
)

data class Category(
    val id: Int,
    val name: String,
    val icon: String
)