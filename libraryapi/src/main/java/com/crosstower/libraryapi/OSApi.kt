package com.crosstower.libraryapi

import com.crosstower.libraryapi.interceptors.AuthInterceptor
import com.crosstower.libraryapi.interfaces.OSApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/***
 * Retrofit instance for api calls
 */
public object OSApi {

    private val okHttp = OkHttpClient.Builder()
        .addNetworkInterceptor(AuthInterceptor())
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okHttp)
        .baseUrl("https://api.imgur.com/3/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val api = retrofit.create(OSApi::class.java)
}
