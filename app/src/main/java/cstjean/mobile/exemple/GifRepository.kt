package cstjean.mobile.exemple

import cstjean.mobile.exemple.api.Gif
import cstjean.mobile.exemple.api.GiphyApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import  retrofit2.create

class GifRepository {
    private val giphyApi: GiphyApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.giphy.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        giphyApi = retrofit.create()
    }

    suspend fun fetchTrending(): List<Gif> = giphyApi.fetchTrending().data

}
