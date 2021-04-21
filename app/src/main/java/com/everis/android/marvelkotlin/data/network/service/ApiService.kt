package com.everis.android.marvelkotlin.data.network.service

import com.everis.android.marvelkotlin.data.network.model.ApiResult
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("public/characters")
    suspend fun getAllCharacters(
        @Query("ts") timestamp: String,
        @Query("hash") hash: String,
        @Query("apikey") apikey: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<ApiResult>

    @GET("public/characters/{characterId}")
    fun getCharacterById(
        @Path("characterId") characterId: Int,
        @Query("ts") timestamp: String,
        @Query("hash") hash: String,
        @Query("apikey") apikey: String
    ): Call<ApiResult>
}