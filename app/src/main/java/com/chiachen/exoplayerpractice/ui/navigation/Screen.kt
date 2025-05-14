package com.chiachen.exoplayerpractice.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.chiachen.exoplayerpractice.utils.Constants

sealed class Screen(val route: String, val icon: @Composable () -> Unit, val label: String) {
    object OnlineVideos : Screen(
        route = Constants.VIDEO_LIST,
        icon = { Icon(Icons.Default.VideoLibrary, contentDescription = "Online videos") },
        label = "Online"
    )
    object DownloadedVideos : Screen(
        route = Constants.VIDEO_DOWNLOADS,
        icon = { Icon(Icons.Default.Download, contentDescription = "Downloaded videos") },
        label = "Downloads"
    )
} 