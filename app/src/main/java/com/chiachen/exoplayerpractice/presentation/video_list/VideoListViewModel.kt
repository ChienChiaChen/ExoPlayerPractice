package com.chiachen.exoplayerpractice.presentation.video_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.chiachen.exoplayerpractice.data.api.RetrofitInstance
import com.chiachen.exoplayerpractice.data.repository.VideoRepository
import com.chiachen.exoplayerpractice.utils.FileObserverManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VideoListViewModel : ViewModel() {
    private val repository = VideoRepository(RetrofitInstance.api)

    val videos = repository.getVideoPagingFlow()
        .cachedIn(viewModelScope)

    private val _downloadingIds = MutableStateFlow<Set<Int>>(emptySet())
    val downloadingIds: StateFlow<Set<Int>> = _downloadingIds.asStateFlow()
    private val _deletedFileNames = MutableStateFlow<Set<Int>>(emptySet())
    val deletedFileNames: StateFlow<Set<Int>> = _deletedFileNames.asStateFlow()

    // Save list scroll position
    private var _firstVisibleItemIndex = 0
    private var _firstVisibleItemScrollOffset = 0

    val firstVisibleItemIndex: Int
        get() = _firstVisibleItemIndex

    val firstVisibleItemScrollOffset: Int
        get() = _firstVisibleItemScrollOffset

    init {
        setupFileObserver()
    }

    private fun setupFileObserver() {
        FileObserverManager.registerObserver()
        viewModelScope.launch {
            FileObserverManager.fileEvents.collect { event ->
                when (event) {
                    is FileObserverManager.FileEvent.FileDeleted -> {
                        val videoId =
                            event.fileName.substringAfter("video_").substringBefore(".mp4")
                                .toIntOrNull()
                        if (videoId != null) {
                            markDeleted(videoId)
                        }
                    }

                    is FileObserverManager.FileEvent.FileCreated -> {
                        val fileName = event.fileName
                        if ("trash" in fileName) return@collect

                        val videoId =
                            event.fileName.substringAfter("video_").substringBefore(".mp4")
                                .toIntOrNull()
                        if (videoId != null) {
                            unmarkDownloading(videoId)
                        }
                    }

                    else -> {
                    } // 其他事件不需要處理
                }
            }
        }
    }

    fun updateScrollPosition(index: Int, offset: Int) {
        _firstVisibleItemIndex = index
        _firstVisibleItemScrollOffset = offset
    }

    fun markDeleted(videoId: Int) {
        _deletedFileNames.update { it + videoId }
    }

    fun markDownloading(videoId: Int) {
        _downloadingIds.update { it + videoId }
    }

    private fun unmarkDownloading(videoId: Int) {
        _downloadingIds.update { it - videoId }
        _deletedFileNames.update { it - videoId }
    }

    override fun onCleared() {
        super.onCleared()
        FileObserverManager.unregisterObserver()
    }
}