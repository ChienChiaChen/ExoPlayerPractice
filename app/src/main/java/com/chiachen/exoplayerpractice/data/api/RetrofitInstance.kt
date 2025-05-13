package com.chiachen.exoplayerpractice.data.api

import com.chiachen.exoplayerpractice.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(
                    Constants.HEADER_AUTHORIZATION,
                    Constants.PEXELS_API_KEY
                )
                .build()
            chain.proceed(request)
        }
        .build()

    val api: PexelsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PexelsApiService::class.java)
    }
}