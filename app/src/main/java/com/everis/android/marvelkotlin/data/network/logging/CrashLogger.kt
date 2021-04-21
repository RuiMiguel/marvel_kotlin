package com.everis.android.marvelkotlin.data.network.logging

import android.util.Log

enum class LogLevel { DEBUG, INFO, WARN, ERROR }

interface CrashLogger {
    fun log(
        exception: Throwable,
        map: Map<String, String?>? = null,
        logLevel: LogLevel = LogLevel.ERROR
    )

    fun log(
        message: String,
        map: Map<String, String?>? = null,
        logLevel: LogLevel = LogLevel.ERROR
    )
}

class DefaultLogger(private val debuggable: Boolean = false) : CrashLogger {

    override fun log(message: String, map: Map<String, String?>?, logLevel: LogLevel) {
        if (debuggable) {
            val tag = "DefaultLogger"
            val msg = "$message - $map"

            when (logLevel) {
                LogLevel.DEBUG -> Log.d(tag, msg)
                LogLevel.INFO -> Log.i(tag, msg)
                LogLevel.WARN -> Log.w(tag, msg)
                LogLevel.ERROR -> Log.e(tag, msg)
            }
        }
    }

    override fun log(exception: Throwable, map: Map<String, String?>?, logLevel: LogLevel) {
        if (debuggable) {
            val tag = "DefaultLogger"
            val msg = "${exception.message} - $map"

            when (logLevel) {
                LogLevel.DEBUG -> Log.d(tag, msg)
                LogLevel.INFO -> Log.i(tag, msg)
                LogLevel.WARN -> Log.w(tag, msg)
                LogLevel.ERROR -> Log.e(tag, msg)
            }
        }
    }
}