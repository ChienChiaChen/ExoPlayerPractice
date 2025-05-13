package com.chiachen.exoplayerpractice.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.chiachen.exoplayerpractice.data.api.PexelsApiService
import com.chiachen.exoplayerpractice.data.model.Video

class VideoPagingSource(
    private val api: PexelsApiService
) : PagingSource<Int, Video>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Video> {
        return try {
            val page = params.key ?: 1
            val response = api.getPopularVideos(page)
            LoadResult.Page(
                data = response.videos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.videos.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Video>): Int? = null
}