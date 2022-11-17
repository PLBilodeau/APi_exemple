package cstjean.mobile.exemple.api

import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "LrEmSjiQfA4tu4xGfRD29FNx38OSJIqQ"

interface GiphyApi {
    @GET("/v1/gifs/trending?api_key=$$API_KEY&limit=10")
    suspend fun  fetchTrending():   GiphyResponse

    @GET("/v1/gifs/search")
    suspend fun search(@Query("q") query: String): GiphyResponse

}