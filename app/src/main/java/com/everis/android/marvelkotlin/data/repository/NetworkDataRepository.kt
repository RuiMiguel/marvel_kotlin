package com.everis.android.marvelkotlin.data.repository

import arrow.core.Either
import com.everis.android.marvelkotlin.data.network.datasource.NetworkDatasource
import com.everis.android.marvelkotlin.domain.error.Failure
import com.everis.android.marvelkotlin.domain.model.Character
import com.everis.android.marvelkotlin.domain.repository.NetworkRepository

class NetworkDataRepository(
    private val networkDatasource: NetworkDatasource
) : NetworkRepository {
    override suspend fun getAllCharacters(
        limit: Int,
        offset: Int
    ): Either<Failure, List<Character>> {
        return networkDatasource.getAllCharacters(limit, offset)
    }

    override fun getCharacterById(id: Int): Either<Failure, Character?> {
        return networkDatasource.getCharacterById(id)
    }
}