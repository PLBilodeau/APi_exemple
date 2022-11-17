package cstjean.mobile.exemple

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

private const val API_KEY = "LrEmSjiQfA4tu4xGfRD29FNx38OSJIqQ"

class GiphyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        val newUrl: HttpUrl = originalRequest.url.newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .build()

        val newRequest: Request = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}