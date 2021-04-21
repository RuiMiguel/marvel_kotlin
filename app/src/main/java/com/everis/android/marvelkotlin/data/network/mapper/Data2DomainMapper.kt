package com.everis.android.marvelkotlin.data.network.mapper

import com.everis.android.marvelkotlin.data.network.model.ApiCharacter
import com.everis.android.marvelkotlin.data.network.model.ApiCharacterThumbnail
import com.everis.android.marvelkotlin.data.network.model.ApiCharacterUrl
import com.everis.android.marvelkotlin.domain.model.Character
import com.everis.android.marvelkotlin.domain.model.CharacterThumbnail
import com.everis.android.marvelkotlin.domain.model.CharacterUrl

fun List<ApiCharacter>?.toCharacterList(): List<Character> = with(this) {
    this?.map { it.toCharacter() } ?: emptyList()
}

fun ApiCharacter.toCharacter(): Character = Character(
    id = this.id ?: 0,
    name = this.name ?: "",
    description = this.description ?: "",
    modified = this.modified ?: "",
    resourceURI = this.resourceURI ?: "",
    urls = this.urls.toCharacterUrlList(),
    thumbnail = this.thumbnail.toCharacterThumbnail()
)

fun List<ApiCharacterUrl>?.toCharacterUrlList(): List<CharacterUrl> =
    this?.map { it.toCharacterUrl() } ?: emptyList()

fun ApiCharacterUrl.toCharacterUrl(): CharacterUrl = CharacterUrl(
    type = this.type ?: "",
    url = this.url ?: ""
)

fun ApiCharacterThumbnail?.toCharacterThumbnail(): CharacterThumbnail = CharacterThumbnail(
    path = this?.path ?: "",
    extension = this?.extension ?: ""
)