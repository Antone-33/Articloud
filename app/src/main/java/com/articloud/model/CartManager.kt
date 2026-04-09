package com.articloud.model

object CartManager {

    data class CartItem(
        val artwork: Artwork,
        var quantity: Int = 1
    )

    private val items = mutableListOf<CartItem>()
    private val listeners = mutableListOf<() -> Unit>()

    fun addItem(artwork: Artwork) {
        val existing = items.find { it.artwork.id == artwork.id }
        if (existing != null) {
            existing.quantity++
        } else {
            items.add(CartItem(artwork))
        }
        notifyListeners()
    }

    fun removeItem(artworkId: Int) {
        items.removeAll { it.artwork.id == artworkId }
        notifyListeners()
    }

    fun increaseQuantity(artworkId: Int) {
        items.find { it.artwork.id == artworkId }?.let {
            it.quantity++
            notifyListeners()
        }
    }

    fun decreaseQuantity(artworkId: Int) {
        val item = items.find { it.artwork.id == artworkId } ?: return
        if (item.quantity > 1) {
            item.quantity--
        } else {
            items.remove(item)
        }
        notifyListeners()
    }

    fun getItems(): List<CartItem> = items.toList()

    fun getTotalCount(): Int = items.sumOf { it.quantity }

    fun getSubtotal(): Double = items.sumOf { it.artwork.price * it.quantity }

    fun hasFreeShipping(): Boolean = getSubtotal() >= 500.0

    fun clear() {
        items.clear()
        notifyListeners()
    }

    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: () -> Unit) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it() }
    }
}