package com.chiachen.exoplayerpractice.data.api

import com.chiachen.exoplayerpractice.data.model.PexelsVideoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PexelsApiService {
    @GET("videos/popular")
    suspend fun getPopularVideos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 10
    ): PexelsVideoResponse
}