package com.everis.android.marvelkotlin.domain.usecase

import arrow.core.Either
import arrow.core.Either.Right
import com.everis.android.marvelkotlin.domain.error.Failure
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

class DoWait : UseCase<Unit, DoWait.Params>() {
    private val _DEFAULT_TIME = 3L

    override fun run(params: Params?): Either<Failure, Unit> = runBlocking {
        val waitTime = when (params) {
            is Params -> params.time ?: _DEFAULT_TIME
            else -> _DEFAULT_TIME
        }

        delay(TimeUnit.SECONDS.toMillis(waitTime))
        Right(Unit)
    }

    class Params private constructor(val time: Long?) {
        companion object {
            fun forTime(time: Long?): Params {
                return Params(time)
            }
        }
    }
}