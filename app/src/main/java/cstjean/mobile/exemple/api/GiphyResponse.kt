package cstjean.mobile.exemple.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GiphyResponse(
    val data: List<Gif>
)
