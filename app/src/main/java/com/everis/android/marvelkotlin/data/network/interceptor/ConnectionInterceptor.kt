package com.everis.android.marvelkotlin.data.network.interceptor

import com.everis.android.marvelkotlin.data.network.logging.CrashLogger
import com.everis.android.marvelkotlin.data.network.logging.mapFromRequest
import com.everis.android.marvelkotlin.data.network.status.NetworkHandler
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@Suppress("unused")
class ConnectionInterceptor(
    private val networkHandler: NetworkHandler,
    private val crashLogger: CrashLogger
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        when (networkHandler.isConnected) {
            true -> {
                return try {
                    chain.proceed(request)
                } catch (timeoutException: SocketTimeoutException) {
                    crashLogger.log(
                        exception = timeoutException,
                        map = mapFromRequest(request)
                    )
                    throw timeoutException
                } catch (unknownHostException: UnknownHostException) {
                    crashLogger.log(
                        exception = unknownHostException,
                        map = mapFromRequest(request)
                    )
                    throw unknownHostException
                } catch (e: Exception) {
                    val exception = IOException("Network error ${e.message}")
                    crashLogger.log(
                        exception = exception,
                        map = mapFromRequest(request)
                    )
                    throw exception
                }
            }
            false -> {
                val exception = NoConnectionException()
                crashLogger.log(
                    exception = exception,
                    map = mapFromRequest(request)
                )
                throw exception
            }
        }
    }
}

class NoConnectionException(msg: String = "Network not connected!") : IOException(msg)