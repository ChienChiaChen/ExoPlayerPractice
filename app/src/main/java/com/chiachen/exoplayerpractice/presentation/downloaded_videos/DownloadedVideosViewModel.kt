package com.chiachen.exoplayerpractice.presentation.downloaded_videos

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chiachen.exoplayerpractice.utils.Constants
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
    }

    private fun loadVideoFiles() {
        viewModelScope.launch {
            val videoDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                Constants.DOWNLOAD_FOLDER_NAME
            )
            _videoFiles.value = videoDir.listFiles()
                ?.filter { it.name.startsWith("video_") }
                ?.toList()
                .orEmpty()
        }
    }

    fun refreshVideoFiles() {
        loadVideoFiles()
    }
} 