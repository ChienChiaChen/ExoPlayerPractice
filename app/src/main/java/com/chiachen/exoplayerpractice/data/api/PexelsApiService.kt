package com.chiachen.exoplayerpractice.data.api

import com.chiachen.exoplayerpractice.data.model.PexelsVideoResponse
import com.chiachen.exoplayerpractice.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface PexelsApiService {
    @GET("videos/popular")
    suspend fun getPopularVideos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = Constants.NETWORK_PAGE_SIZE
    ): PexelsVideoResponse
}