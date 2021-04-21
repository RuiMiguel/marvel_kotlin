package com.everis.android.marvelkotlin.data.network.error

import com.everis.android.marvelkotlin.domain.error.Failure

sealed class NetworkFailure(data: Any? = null) : Failure.FeatureFailure(data) {
    class ServerFailure(val code: String? = "0", message: Any? = null) : NetworkFailure(message)
    object Timeout : NetworkFailure()
    object UnknownHost : NetworkFailure()
    class JsonFormat(msg: String?) : NetworkFailure(msg)
    object NoInternetConnection : NetworkFailure()
    object NotAuthorized : NetworkFailure()
    object Untrusted : NetworkFailure()
    object NetworkConnection : NetworkFailure()
}