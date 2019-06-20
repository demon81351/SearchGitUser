package com.example.searchgituser.http

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.TimeUnit

class ResourceFactory(private val baseUrl: String) {

    private var encryptHttpClient: OkHttpClient? = null

    @Synchronized
    private fun getHttpClient(): OkHttpClient {

        val client: OkHttpClient
        if (encryptHttpClient != null) {
            client = encryptHttpClient as OkHttpClient
        } else {
            encryptHttpClient = buildBaseHttpClient(RequestInterceptor())
            client = encryptHttpClient as OkHttpClient
        }

        return client
    }

    private fun buildBaseHttpClient(interceptor: RequestInterceptor): OkHttpClient {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(loggingInterceptor)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

    }

    fun <S> createService(serviceClass: Class<S>): S {

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getHttpClient())
                .build()
                .create(serviceClass)
    }

}