package com.crosstower.libraryapi.interceptors

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

/***
 * Interceptor for retrofit headers and response
 * CLIENT_ID = imgur open source client id
 */
class AuthInterceptor: Interceptor {
    companion object {
        private const val CLIENT_ID = "42958bb5c4d4b32"
    }

    override fun intercept(chain: Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Client-ID $CLIENT_ID")
            .build()


        val response = chain.proceed(request)
        return response
    }
}