package com.chiachen.exoplayerpractice.presentation.downloaded_videos

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chiachen.exoplayerpractice.utils.Constants
import com.chiachen.exoplayerpractice.utils.FileObserverManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class DownloadedVideosViewModel : ViewModel() {
    private val _videoFiles = MutableStateFlow<List<File>>(emptyList())
    val videoFiles: StateFlow<List<File>> = _videoFiles.asStateFlow()

    init {
        loadVideoFiles()
        setupFileObserver()
    }

    private fun setupFileObserver() {
        FileObserverManager.registerObserver()
        viewModelScope.launch {
            FileObserverManager.fileEvents.collect { event ->
                when (event) {
                    is FileObserverManager.FileEvent.FileCreated,
                    is FileObserverManager.FileEvent.FileDeleted,
                    is FileObserverManager.FileEvent.FileModified -> {
                        loadVideoFiles()
                    }
                }
            }
        }
    }

    private fun loadVideoFiles() {
        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val videoDir = File(downloadDir, Constants.DOWNLOAD_FOLDER_NAME)
        if (videoDir.exists()) {
            _videoFiles.value = videoDir.listFiles()?.filter { it.name.startsWith("video_") }
                ?.filter { it.isFile }
                ?.toList()
                ?: emptyList()
        } else {
            _videoFiles.value = emptyList()
        }
    }

    override fun onCleared() {
        super.onCleared()
        FileObserverManager.unregisterObserver()
    }
} 