package com.everis.android.marvelkotlin.domain.usecase

import arrow.core.Either
import arrow.core.left
import com.everis.android.marvelkotlin.domain.error.Failure
import com.everis.android.marvelkotlin.domain.model.Character
import com.everis.android.marvelkotlin.domain.repository.NetworkRepository

class GetCharacter(
    private val networkRepository: NetworkRepository
) : UseCase<Character?, GetCharacter.Params>() {
    override fun run(params: Params?): Either<Failure, Character?> {
        return when (params) {
            is Params -> {
                networkRepository.getCharacterById(params.id)
            }
            else -> {
                Failure.GenericFailure(data = "Missing param id").left()
            }
        }
    }

    class Params private constructor(val id: Int) {
        companion object {
            fun forId(id: Int): Params {
                return Params(id)
            }
        }
    }
}