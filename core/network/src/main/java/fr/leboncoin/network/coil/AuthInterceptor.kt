package fr.leboncoin.network.coil

import okhttp3.Interceptor
import okhttp3.Response

class CoilHeadersInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("User-Agent", "LeboncoinApp/1.0")
            .build()
        return chain.proceed(request)
    }
}