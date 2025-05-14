package com.chiachen.exoplayerpractice.presentation.video_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.chiachen.exoplayerpractice.data.api.RetrofitInstance
import com.chiachen.exoplayerpractice.data.repository.VideoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VideoListViewModel : ViewModel() {
    private val repository = VideoRepository(RetrofitInstance.api)

    val videos = repository.getVideoPagingFlow()
        .cachedIn(viewModelScope)

    private val _downloadingIds = MutableStateFlow<Set<Int>>(emptySet())
    val downloadingIds: StateFlow<Set<Int>> = _downloadingIds.asStateFlow()

    // Save list scroll position
    private var _firstVisibleItemIndex = 0
    private var _firstVisibleItemScrollOffset = 0

    val firstVisibleItemIndex: Int
        get() = _firstVisibleItemIndex

    val firstVisibleItemScrollOffset: Int
        get() = _firstVisibleItemScrollOffset

    fun updateScrollPosition(index: Int, offset: Int) {
        _firstVisibleItemIndex = index
        _firstVisibleItemScrollOffset = offset
    }

    fun markDownloading(videoId: Int) {
        _downloadingIds.update { it + videoId }
    }

    fun unmarkDownloading(videoId: Int) {
        _downloadingIds.update { it - videoId }
    }
}