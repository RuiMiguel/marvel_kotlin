package com.everis.android.marvelkotlin.domain.model

data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val modified: String,
    val resourceURI: String,
    val urls: List<CharacterUrl>,
    val thumbnail: CharacterThumbnail
)

data class CharacterUrl(
    val type: String,
    val url: String
)

data class CharacterThumbnail(
    val path: String,
    val extension: String
)