package com.everis.android.marvelkotlin.domain.repository

import arrow.core.Either
import com.everis.android.marvelkotlin.domain.error.Failure
import com.everis.android.marvelkotlin.domain.model.Character

interface NetworkRepository {
    suspend fun getAllCharacters(limit: Int, offset: Int): Either<Failure, List<Character>>
    fun getCharacterById(id: Int): Either<Failure, Character?>
}