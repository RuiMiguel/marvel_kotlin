package com.everis.android.marvelkotlin.domain.usecase

import arrow.core.Either
import arrow.core.left
import com.everis.android.marvelkotlin.domain.error.Failure
import com.everis.android.marvelkotlin.domain.model.Character
import com.everis.android.marvelkotlin.domain.repository.NetworkRepository

class GetCharacters(
    private val networkRepository: NetworkRepository
) : UseCaseSuspend<List<Character>, GetCharacters.Params>() {
    override suspend fun run(params: Params?): Either<Failure, List<Character>> {
        val (limit, offset) =  when (params) {
            is Params -> {
                Pair(params.limit, params.offset)
            }
            else -> {
                Pair(50, 0)
            }
        }

        return networkRepository.getAllCharacters(limit, offset)
    }

    class Params private constructor(val limit: Int, val offset: Int) {
        companion object {
            fun forFilter(limit: Int, offset: Int): Params {
                return Params(limit, offset)
            }
        }
    }
}