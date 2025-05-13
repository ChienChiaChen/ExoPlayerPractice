package com.chiachen.exoplayerpractice.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.pexels.com/"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(
                    "Authorization",
                    "p5CSwNxT7T2tUKIebcVYswPectT7KFnibDztowHnNIayYcA62HqLqjYp"
                )
                .build()
            chain.proceed(request)
        }
        .build()

    val api: PexelsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PexelsApiService::class.java)
    }
}