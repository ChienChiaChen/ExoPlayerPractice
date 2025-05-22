package com.chiachen.exoplayerpractice.presentation.video_list

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
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
    val deletedFileNames by viewModel.deletedFileNames.collectAsState()

    // Use rememberLazyListState to save list state
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = viewModel.firstVisibleItemIndex,
        initialFirstVisibleItemScrollOffset = viewModel.firstVisibleItemScrollOffset
    )

    // Update scroll position in ViewModel when list scrolls
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                viewModel.updateScrollPosition(index, offset)
            }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Video List") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            state = listState
        ) {
            items(
                count = videos.itemCount,
                key = { index -> videos[index]?.id ?: index }
            ) { index ->
                val video = videos[index]
                if (video != null) {
                    val fileName = "video_${video.id}.mp4"
                    val isDownloaded =
                        DownloadHelper.isVideoDownloaded(fileName) &&
                                !deletedFileNames.contains(video.id)
                    val isDownloading = downloadingIds.contains(video.id)
                    val videoLink = video.videoFiles.firstOrNull()?.link.orEmpty()
                    VideoItem(
                        videoUrl = video.url,
                        videoImage = video.image,
                        videoLink = video.videoFiles.firstOrNull()?.link.orEmpty(),
                        isDownloaded = isDownloaded,
                        isDownloading = isDownloading,
                        onVideoClick = {
                            navController.navigate(
                                "${Constants.VIDEO_PLAYER}?videoUrl=${Uri.encode(videoLink)}"
                            )
                        },
                        onDownloadClick = {
                            DownloadHelper.downloadVideo(context, videoLink, fileName)
                            viewModel.markDownloading(video.id)
                        }
                    )
                }
            }

            videos.apply {
                when {
                    loadState.refresh is LoadState.Loading ->
                        item { LoadingItem() }
                    loadState.append is LoadState.Loading ->
                        item { LoadingItem() }

                    loadState.refresh is LoadState.Error ->
                        item {
                            ErrorItem(
                                message = (loadState.refresh as LoadState.Error).error.localizedMessage
                                    ?: "Error loading videos",
                                onRetryClick = { retry() }
                            )
                        }

                    loadState.append is LoadState.Error ->
                        item {
                            ErrorItem(
                                message = (loadState.append as LoadState.Error).error.localizedMessage
                                    ?: "Error loading more videos",
                                onRetryClick = { retry() }
                            )
                        }

                }
            }
        }
    }
}