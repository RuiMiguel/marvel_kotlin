package com.everis.android.marvelkotlin.data.network.datasource

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.left
import arrow.core.right
import com.everis.android.marvelkotlin.data.network.error.NetworkFailure
import com.everis.android.marvelkotlin.data.network.interceptor.NoConnectionException
import com.everis.android.marvelkotlin.data.network.logging.CrashLogger
import com.everis.android.marvelkotlin.data.network.logging.DefaultLogger
import com.everis.android.marvelkotlin.data.network.logging.NetworkException
import com.everis.android.marvelkotlin.data.network.logging.NetworkLoggerHelper.LOG_RESPONSE_BODY
import com.everis.android.marvelkotlin.data.network.logging.NetworkLoggerHelper.LOG_RESPONSE_CODE
import com.everis.android.marvelkotlin.data.network.logging.NetworkLoggerHelper.LOG_RESPONSE_MESSAGE
import com.everis.android.marvelkotlin.data.network.logging.mapFromResponse
import com.everis.android.marvelkotlin.data.network.model.BaseNetworkApiResponse
import com.everis.android.marvelkotlin.domain.error.Failure
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseNetworkDatasource(private val crashLogger: CrashLogger) {
    private val UNKNOWN_ERROR_CODE = "-1"
    private val NO_CONNECTION = "-400"
    private val HTTP_EXCEPTION_CODE = "-401"
    private val TIMEOUT = "-402"
    private val UNKNOWN_HOST = "-403"
    private val JSON_FORMAT = "-404"

    protected open suspend fun <Api : BaseNetworkApiResponse, Domain> requestApi(
        call: Response<Api>,
        parserSuccess: (Api?) -> Domain
    ): Either<Failure, Domain> {
        return try {
            parseResponse(call, parserSuccess).mapLeft(::parseToFailure)
        } catch (error: IOException) {
            manageRequestException<Domain>(error).mapLeft(::parseToFailure)
        }
    }

    protected open fun <Api : BaseNetworkApiResponse, Domain> requestApi(
        call: Call<Api>,
        parserSuccess: (Api?) -> Domain
    ): Either<Failure, Domain> {
        return try {
            parseResponse(call.execute(), parserSuccess).mapLeft(::parseToFailure)
        } catch (error: IOException) {
            manageRequestException<Domain>(error).mapLeft(::parseToFailure)
        }
    }

    private fun <Api : BaseNetworkApiResponse?, Domain> parseResponse(
        response: Response<Api>,
        parserSuccess: (Api?) -> Domain
    ): Either<BaseNetworkApiResponse, Domain> {
        return if (response.isSuccessful) {
            parseApiResponse(response, parserSuccess)
        } else {
            parseErrorBody(response)
        }
    }

    private fun <Api : BaseNetworkApiResponse?, Domain> parseApiResponse(
        response: Response<Api>,
        parserSuccess: (Api?) -> Domain
    ): Either<BaseNetworkApiResponse, Domain> {
        val data = response.body()
        return if (isSuccessful(response, data)) {
            parserSuccess(data).right()
        } else {
            parseError(response)
        }
    }

    private fun <Api : BaseNetworkApiResponse?> isSuccessful(
        response: Response<Api>,
        data: Api?
    ): Boolean =
        response.isSuccessful && (data == null || data.isSuccess())

    private fun <Error : BaseNetworkApiResponse?, Domain> parseError(response: Response<Error>): Either<BaseNetworkApiResponse, Domain> {
        crashLogger.log(
            exception = NetworkException("parseError"),
            map = mapFromResponse(response)
        )

        return when (val error = response.body()) {
            is BaseNetworkApiResponse -> throwError(
                code = error.errorCode() ?: UNKNOWN_ERROR_CODE,
                message = error.errorData()
            ).left()
            else -> {
                parseErrorBody(response)
            }
        }
    }

    private fun <Domain> parseErrorBody(response: Response<*>): Either<BaseNetworkApiResponse, Domain> {
        val errorBody = response.errorBody()?.string()

        crashLogger.log(
            exception = NetworkException("parseErrorBody"),
            map = mapFromResponse(response)
                .also { map ->
                    map[LOG_RESPONSE_BODY] = errorBody
                }
        )

        return try {
            parseErrorType(
                code = response.code(),
                message = response.message(),
                body = errorBody
            ).left()
        } catch (e: Exception) {
            Left(throwError(response.code().toString(), e.message))
        }
    }

    private fun <Domain> manageRequestException(error: Throwable): Either<BaseNetworkApiResponse, Domain> {
        crashLogger.log(
            exception = NetworkException("manageRequestException"),
            map = mapOf(LOG_RESPONSE_MESSAGE to error.message)
        )

        return when (error) {
            is NoConnectionException -> Left(throwError(code = NO_CONNECTION))
            is SocketTimeoutException -> Left(throwError(code = TIMEOUT))
            is UnknownHostException -> Left(throwError(code = UNKNOWN_HOST))
            else -> Left(throwError(code = HTTP_EXCEPTION_CODE, message = error.message))
        }
    }

    private fun parseToFailure(networkApiError: BaseNetworkApiResponse): NetworkFailure {
        crashLogger.log(
            exception = NetworkException("manageGenericRequestException"),
            map = mapOf(
                LOG_RESPONSE_CODE to networkApiError.errorCode(),
                LOG_RESPONSE_MESSAGE to networkApiError.errorData().toString()
            )
        )

        return when (networkApiError.errorCode()) {
            NO_CONNECTION -> NetworkFailure.NoInternetConnection
            TIMEOUT -> NetworkFailure.Timeout
            UNKNOWN_HOST -> NetworkFailure.UnknownHost
            JSON_FORMAT -> NetworkFailure.JsonFormat(networkApiError.errorData().toString())
            else -> NetworkFailure.ServerFailure(
                networkApiError.errorCode(),
                networkApiError.errorData()
            )
        }
    }

    private fun throwError(code: String?, message: Any? = null): BaseNetworkApiResponse =
        object :
            BaseNetworkApiResponse() {
            override fun isSuccess(): Boolean = false
            override fun errorCode(): String? = code
            override fun errorData(): Any? = message
        }

    open fun parseErrorType(code: Int, message: Any?, body: String?): BaseNetworkApiResponse =
        object :
            BaseNetworkApiResponse() {
            override fun isSuccess(): Boolean = false
            override fun errorCode(): String? = code.toString()
            override fun errorData(): Any? = message
        }
}