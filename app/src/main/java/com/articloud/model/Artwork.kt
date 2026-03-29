package com.articloud.model

data class Artwork(
    val id: Int,
    val name: String,
    val price: Double,
    val image: String, //  URL
    val description: String
)