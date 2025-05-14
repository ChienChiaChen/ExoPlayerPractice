package com.chiachen.exoplayerpractice.presentation.video_list

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.chiachen.exoplayerpractice.presentation.components.ErrorItem
import com.chiachen.exoplayerpractice.presentation.components.LoadingItem
import com.chiachen.exoplayerpractice.presentation.components.VideoItem
import com.chiachen.exoplayerpractice.utils.Constants
import com.chiachen.exoplayerpractice.utils.DownloadHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoListScreen(
    navController: NavController,
    viewModel: VideoListViewModel = viewModel()
) {
    val context = LocalContext.current
    val videos = viewModel.videos.collectAsLazyPagingItems()
    val downloadingIds by viewModel.downloadingIds.collectAsState()
    val downloadIdToVideoId = remember { mutableStateMapOf<Long, Int>() }

    // Register receiver once per screen
    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context, intent: Intent) {
                if (intent.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                    val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
                    val videoId = downloadIdToVideoId[downloadId]
                    if (videoId != null) {
                        viewModel.unmarkDownloading(videoId)
                    }
                }
            }
        }
        context.registerReceiver(
            receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            Context.RECEIVER_NOT_EXPORTED
        )
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Video List") }) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            LazyColumn {
                items(
                    count = videos.itemCount,
                    key = videos.itemKey { it.id }
                ) { index ->
                    val video = videos[index]
                    if (video != null) {
                        VideoItem(
                            video = video,
                            isDownloading = downloadingIds.contains(video.id),
                            onVideoClick = { videoUrl ->
                                navController.navigate(
                                    "${Constants.VIDEO_PLAYER}?videoUrl=${Uri.encode(videoUrl)}"
                                )
                            },
                            onDownloadClick = { videoUrl ->
                                val fileName = "video_${video.id}.mp4"
                                val downloadId = DownloadHelper.downloadVideo(
                                    context,
                                    videoUrl,
                                    fileName
                                )
                                downloadIdToVideoId[downloadId] = video.id
                                viewModel.markDownloading(video.id)
                            }
                        )
                    }
                }

                videos.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item { LoadingItem() }
                        }
                        loadState.append is LoadState.Loading -> {
                            item { LoadingItem() }
                        }
                        loadState.refresh is LoadState.Error -> {
                            item {
                                ErrorItem(
                                    message = (loadState.refresh as LoadState.Error).error.localizedMessage ?: "Error loading videos",
                                    onRetryClick = { retry() }
                                )
                            }
                        }
                        loadState.append is LoadState.Error -> {
                            item {
                                ErrorItem(
                                    message = (loadState.append as LoadState.Error).error.localizedMessage ?: "Error loading more videos",
                                    onRetryClick = { retry() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}