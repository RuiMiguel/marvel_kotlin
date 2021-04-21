package com.everis.android.marvelkotlin.domain.usecase

import arrow.core.Either
import com.everis.android.marvelkotlin.domain.error.Failure

abstract class UseCase<out Type, in Params> where Type : Any? {
    abstract fun run(params: Params? = null): Either<Failure, Type>

    @JvmOverloads
    operator fun invoke(
        params: Params? = null
    ): Either<Failure, Type> {
        return run(params)
    }
}

abstract class UseCaseSuspend<out Type, in Params> where Type : Any? {
    abstract suspend fun run(params: Params? = null): Either<Failure, Type>

    @JvmOverloads
    suspend operator fun invoke(
        params: Params? = null
    ): Either<Failure, Type> {
        return run(params)
    }
}