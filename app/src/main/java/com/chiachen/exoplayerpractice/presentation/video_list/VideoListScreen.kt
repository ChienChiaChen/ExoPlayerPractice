package com.chiachen.exoplayerpractice.presentation.video_list

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
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
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(
                count = videos.itemCount,
                key = { index -> videos[index]?.id ?: index }
            ) { index ->
                val video = videos[index]
                if (video != null) {
                    val fileName = "video_${video.id}.mp4"
                    val isDownloaded = DownloadHelper.isVideoDownloaded(fileName)
                    val isDownloading = downloadingIds.contains(video.id)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                val videoUrl =
                                    video.videoFiles.firstOrNull()?.link ?: return@clickable
                                navController.navigate(
                                    "${Constants.VIDEO_PLAYER}?videoUrl=${
                                        Uri.encode(
                                            videoUrl
                                        )
                                    }"
                                )
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column {
                            AsyncImage(
                                model = video.image,
                                contentDescription = "Video Thumbnail",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16 / 9f),
                                contentScale = ContentScale.Crop
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = video.url,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .weight(1f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                IconButton(
                                    onClick = {
                                        val videoUrl = video.videoFiles.firstOrNull()?.link
                                            ?: return@IconButton
                                        val downloadId = DownloadHelper.downloadVideo(
                                            context,
                                            videoUrl,
                                            fileName
                                        )
                                        downloadIdToVideoId[downloadId] = video.id
                                        viewModel.markDownloading(video.id)
                                    },
                                    enabled = !isDownloaded && !isDownloading
                                ) {
                                    when {
                                        isDownloaded -> Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Downloaded"
                                        )

                                        isDownloading -> CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            strokeWidth = 2.dp
                                        )

                                        else -> Icon(
                                            Icons.Default.Download,
                                            contentDescription = "downloading"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}