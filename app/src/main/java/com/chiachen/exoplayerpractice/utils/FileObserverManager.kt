package com.chiachen.exoplayerpractice.utils

import android.os.Environment
import android.os.FileObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.io.File

object FileObserverManager {
    private var fileObserver: FileObserver? = null
    private val _fileEvents = MutableSharedFlow<FileEvent>()
    val fileEvents: SharedFlow<FileEvent> = _fileEvents
    private var observerCount = 0
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var cachedFiles: Set<String> = emptySet()

    sealed class FileEvent(open val fileName: String) {
        data class FileCreated(override val fileName: String) : FileEvent(fileName)
        data class FileDeleted(override val fileName: String) : FileEvent(fileName)
        data class FileModified(override val fileName: String) : FileEvent(fileName)
    }

    fun registerObserver() {
        observerCount++
        if (observerCount == 1) {
            startObserving()
        }
    }

    fun unregisterObserver() {
        observerCount--
        if (observerCount == 0) {
            stopObserving()
        }
    }

    private fun startObserving() {
        if (fileObserver != null) return

        val videoDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            Constants.DOWNLOAD_FOLDER_NAME
        )

        fileObserver = object : FileObserver(videoDir.absolutePath) {
            override fun onEvent(event: Int, path: String?) {
                if (path == null) return
                
                when (event) {
                    FileObserver.CREATE -> {
                        scope.launch {
                            _fileEvents.emit(FileEvent.FileCreated(path))
                            cachedFiles = cachedFiles + path
                        }
                    }
                    FileObserver.DELETE -> {
                        scope.launch {
                            _fileEvents.emit(FileEvent.FileDeleted(path))
                            cachedFiles = cachedFiles - path
                        }
                    }
                    FileObserver.MODIFY -> {
                        scope.launch {
                            _fileEvents.emit(FileEvent.FileModified(path))
                        }
                    }
                }
            }
        }.apply {
            startWatching()
        }
    }

    private fun stopObserving() {
        fileObserver?.stopWatching()
        fileObserver = null
    }

    fun syncCurrentFiles() {
        val videoDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            Constants.DOWNLOAD_FOLDER_NAME
        )
        val currentFiles = videoDir.listFiles()?.map { it.name }?.toSet() ?: emptySet()

        val deletedFiles = cachedFiles - currentFiles
        val addedFiles = currentFiles - cachedFiles

        scope.launch {
            deletedFiles.forEach {
                _fileEvents.emit(FileEvent.FileDeleted(it))
            }
            addedFiles.forEach {
                _fileEvents.emit(FileEvent.FileCreated(it))
            }
        }

        cachedFiles = currentFiles
    }
} 