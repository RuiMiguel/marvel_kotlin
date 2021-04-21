package com.everis.android.marvelkotlin.data.network.model

import com.squareup.moshi.Json

open class BaseNetworkApiResponse {
    open fun isSuccess(): Boolean = false
    open fun errorCode(): String? = null
    open fun errorData(): Any? = null
}

class ApiError(
    @Json(name = "code") val code: String? = null,
    @Json(name = "message") val message: String? = null
) : BaseNetworkApiResponse(
) {
    override fun errorCode(): String? = code ?: "-1"
    override fun errorData(): String? = message ?: "ERROR"
    override fun isSuccess(): Boolean = false
}

class ApiResult(
    @Json(name = "code") val code: Int?,
    @Json(name = "status") val status: String?,
    @Json(name = "copyright") val copyright: String?,
    @Json(name = "attributionText") val attributionText: String?,
    @Json(name = "attributionHTML") val attributionHTML: String?,
    @Json(name = "data") val data: ApiData?,
    @Json(name = "results") val results: String?,
) : BaseNetworkApiResponse() {
    override fun errorCode(): String? = ""
    override fun errorData(): String? = ""
    override fun isSuccess(): Boolean = true
}

class ApiData(
    @Json(name = "offset") val offset: Int?,
    @Json(name = "limit") val limit: Int?,
    @Json(name = "total") val total: Int?,
    @Json(name = "count") val count: Int?,
    @Json(name = "results") val results: List<ApiCharacter>?,
)

class ApiCharacter(
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "modified") val modified: String?,
    @Json(name = "resourceURI") val resourceURI: String?,
    @Json(name = "urls") val urls: List<ApiCharacterUrl>?,
    @Json(name = "thumbnail") val thumbnail: ApiCharacterThumbnail?
) : BaseNetworkApiResponse() {
    override fun errorCode(): String? = ""
    override fun errorData(): String? = ""
    override fun isSuccess(): Boolean = true
}

class ApiCharacterUrl(
    @Json(name = "type") val type: String?,
    @Json(name = "url") val url: String?
)

class ApiCharacterThumbnail(
    @Json(name = "path") val path: String?,
    @Json(name = "extension") val extension: String?
)

