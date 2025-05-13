package com.chiachen.exoplayerpractice.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File

object DownloadHelper {
    fun downloadVideo(
        context: Context,
        videoUrl: String,
        fileName: String,
    ): Long {

        val request = DownloadManager.Request(Uri.parse(videoUrl)).apply {
            setTitle("Pexels Video")
            setDescription(fileName)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${Constants.DOWNLOAD_FOLDER_NAME}/$fileName")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        }
        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return dm.enqueue(request) // return downloadId
    }

    fun isVideoDownloaded(fileName: String): Boolean {
        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadDir, "${Constants.DOWNLOAD_FOLDER_NAME}/$fileName")
        return file.exists()
    }
}