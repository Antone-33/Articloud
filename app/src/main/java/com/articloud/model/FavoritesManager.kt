package com.articloud.model

object FavoritesManager {

    private val items = mutableListOf<Artwork>()
    private val listeners = mutableListOf<() -> Unit>()

    fun toggle(artwork: Artwork) {
        if (isFavorite(artwork.id)) {
            items.removeAll { it.id == artwork.id }
        } else {
            items.add(artwork)
        }
        notifyListeners()
    }

    fun isFavorite(id: Int): Boolean = items.any { it.id == id }

    fun getItems(): List<Artwork> = items.toList()

    fun getCount(): Int = items.size

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