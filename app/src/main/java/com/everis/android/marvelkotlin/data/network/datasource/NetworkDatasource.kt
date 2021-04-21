package com.everis.android.marvelkotlin.data.network.datasource

import arrow.core.Either
import com.everis.android.marvelkotlin.data.network.logging.CrashLogger
import com.everis.android.marvelkotlin.data.network.mapper.toCharacterList
import com.everis.android.marvelkotlin.data.network.model.ApiError
import com.everis.android.marvelkotlin.data.network.model.BaseNetworkApiResponse
import com.everis.android.marvelkotlin.data.network.parser.MoshiJsonParser
import com.everis.android.marvelkotlin.data.network.service.ApiService
import com.everis.android.marvelkotlin.domain.error.Failure
import com.everis.android.marvelkotlin.domain.model.Character
import java.math.BigInteger
import java.security.MessageDigest

class NetworkDatasource(
    private val apiService: ApiService,
    private val privateKey: String,
    private val publicKey: String,
    crashLogger: CrashLogger
) : BaseNetworkDatasource(crashLogger) {

    suspend fun getAllCharacters(limit: Int, offset: Int): Either<Failure, List<Character>> {
        val (timestamp, hash) = generateMD5()
        return requestApi(
            apiService.getAllCharacters(
                timestamp = timestamp,
                hash = hash,
                apikey = publicKey,
                limit = limit,
                offset = offset
            )
        ) {
            it?.data?.results.toCharacterList()
        }
    }

    fun getCharacterById(characterId: Int): Either<Failure, Character?> {
        val (timestamp, hash) = generateMD5()
        return requestApi(
            apiService.getCharacterById(
                timestamp = timestamp,
                hash = hash,
                apikey = publicKey,
                characterId = characterId
            )
        ) {
            val list = it?.data?.results.toCharacterList()
            if (list.isNotEmpty()) list.first() else null
        }
    }

    private fun generateMD5(): Pair<String, String> {
        val timestamp = System.currentTimeMillis().toString()
        val input = "$timestamp$privateKey$publicKey"
        val md = MessageDigest.getInstance("MD5")
        val hash = BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
        return Pair(timestamp, hash)
    }

    override fun parseErrorType(code: Int, message: Any?, body: String?): BaseNetworkApiResponse {
        return body?.let {
            if (it.isEmpty()) ApiError(
                code = "$code",
                message = "$message"
            )
            else MoshiJsonParser().fromJson(body, ApiError::class.java)
        } ?: ApiError(code = "$code", message = "$message")
    }
}