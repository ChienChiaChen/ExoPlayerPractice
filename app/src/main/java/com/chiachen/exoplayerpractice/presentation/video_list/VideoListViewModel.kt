package com.chiachen.exoplayerpractice.presentation.video_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.chiachen.exoplayerpractice.data.api.RetrofitInstance
import com.chiachen.exoplayerpractice.data.repository.VideoRepository

class VideoListViewModel : ViewModel() {
    private val repository = VideoRepository(RetrofitInstance.api)

    val videos = repository.getVideoPagingFlow()
        .cachedIn(viewModelScope)
}