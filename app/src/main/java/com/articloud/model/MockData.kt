package com.articloud.model

object MockData {

    val categories = listOf(
        Category(1, "Abstracto", "●"),
        Category(2, "Paisaje", "▲"),
        Category(3, "Figurativo", "◆"),
        Category(4, "Urbano", "■"),
        Category(5, "Retrato", "◉"),
        Category(6, "Minimalista", "○")
    )

    val artworks = listOf(
        Artwork(
            id = 1,
            name = "Nebula Dreams",
            artist = "Elena Voss",
            price = 1200.0,
            originalPrice = 1500.0,
            image = "https://images.unsplash.com/photo-1541961017774-22349e4a1262?w=400",
            description = "Una obra maestra que captura la esencia de la forma y el color en perfecta armonía. Creada con Acrílico sobre lienzo, esta pieza única transmite profundidad y emoción a través de cada pincelada cuidadosamente aplicada.",
            category = "Abstracto",
            size = "80x100cm",
            technique = "Acrílico",
            year = 2024,
            rating = 4.9f,
            reviewCount = 47,
            badge = "Más vendido",
            isFeatured = false,
            hasFreeShipping = true
        ),
        Artwork(
            id = 2,
            name = "Golden Horizon",
            artist = "Marco Rivas",
            price = 890.0,
            originalPrice = null,
            image = "https://images.unsplash.com/photo-1578301978693-85fa9c0320b9?w=400",
            description = "Paisaje dorado que evoca la calidez del atardecer en la costa peruana. Técnica mixta sobre tela con acabados en oro.",
            category = "Paisaje",
            size = "60x80cm",
            technique = "Óleo",
            year = 2024,
            rating = 4.7f,
            reviewCount = 23,
            badge = "Nuevo",
            isFeatured = false,
            hasFreeShipping = false
        ),
        Artwork(
            id = 3,
            name = "Silence of Forms",
            artist = "Yuki Tanaka",
            price = 2100.0,
            originalPrice = 2800.0,
            image = "https://images.unsplash.com/photo-1549490349-8643362247b5?w=400",
            description = "Una obra contemplativa que invita al espectador a encontrar el silencio en las formas. Figurativo expresionista de gran formato.",
            category = "Figurativo",
            size = "100x120cm",
            technique = "Óleo",
            year = 2023,
            rating = 5.0f,
            reviewCount = 31,
            badge = "Premium",
            isFeatured = true,
            hasFreeShipping = true
        ),
        Artwork(
            id = 4,
            name = "Crimson Tide",
            artist = "Sara Bloom",
            price = 750.0,
            originalPrice = null,
            image = "https://images.unsplash.com/photo-1605721911519-3dfeb3be25e7?w=400",
            description = "Abstracción en rojos y negros que transmite la fuerza del océano en tempestad. Técnica acrílica de alta textura.",
            category = "Abstracto",
            size = "70x90cm",
            technique = "Acrílico",
            year = 2024,
            rating = 4.8f,
            reviewCount = 18,
            badge = null,
            isFeatured = false,
            hasFreeShipping = false
        ),
        Artwork(
            id = 5,
            name = "Ocean Whisper",
            artist = "Leo Marín",
            price = 1450.0,
            originalPrice = null,
            image = "https://images.unsplash.com/photo-1518998053901-5348d3961a04?w=400",
            description = "Una obra maestra que captura la esencia de la forma y el color en perfecta armonía. Creada con Acuarela sobre lienzo.",
            category = "Paisaje",
            size = "90x110cm",
            technique = "Acuarela",
            year = 2023,
            rating = 4.6f,
            reviewCount = 29,
            badge = "Oferta",
            isFeatured = false,
            hasFreeShipping = true
        ),
        Artwork(
            id = 6,
            name = "Aurora Borealis",
            artist = "Elena Voss",
            price = 3200.0,
            originalPrice = null,
            image = "https://images.unsplash.com/photo-1531366936337-7c912a4589a7?w=400",
            description = "La majestuosidad de la aurora boreal capturada en acrílico y pigmentos fluorescentes sobre tela negra.",
            category = "Paisaje",
            size = "120x150cm",
            technique = "Acrílico",
            year = 2024,
            rating = 4.9f,
            reviewCount = 12,
            badge = "Nuevo",
            isFeatured = false,
            hasFreeShipping = true
        )
    )

    val featuredArtwork = artworks.first { it.isFeatured }
}