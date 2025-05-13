package com.chiachen.exoplayerpractice.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.chiachen.exoplayerpractice.data.api.PexelsApiService
import com.chiachen.exoplayerpractice.data.model.Video
import com.chiachen.exoplayerpractice.data.paging.VideoPagingSource
import kotlinx.coroutines.flow.Flow

class VideoRepository(private val api: PexelsApiService) {
    fun getVideoPagingFlow(): Flow<PagingData<Video>> {
        return Pager(PagingConfig(pageSize = 10)) {
            VideoPagingSource(api)
        }.flow
    }
}