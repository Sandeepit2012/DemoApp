package com.crosstower.libraryapi.di.modules

import com.crosstower.libraryapi.interceptors.AuthInterceptor
import com.crosstower.libraryapi.interfaces.OSApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/***
 * DI module for Dagger to be used by Application
 */
@Module
class OSApiModule {

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(AuthInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://api.imgur.com/3/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideImgApi(retrofit: Retrofit): OSApi {
        return retrofit.create(OSApi::class.java)
    }
}