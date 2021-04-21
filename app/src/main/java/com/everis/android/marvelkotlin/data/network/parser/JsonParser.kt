package com.everis.android.marvelkotlin.data.network.parser

interface JsonParser {
    fun <T> fromJson(json: String, type: Class<T>): T?
    fun <T : Any> toJson(value: T): String
}
