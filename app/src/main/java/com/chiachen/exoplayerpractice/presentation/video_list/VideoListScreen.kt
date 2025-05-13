package com.chiachen.exoplayerpractice.presentation.video_list

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun VideoListScreen(
    navController: NavController,
    viewModel: VideoListViewModel = viewModel()
) {
    val videos = viewModel.videos.collectAsLazyPagingItems()

    LazyColumn {
        items(
            count = videos.itemCount,
            key = { index -> videos[index]?.id ?: index }
        ) { index ->
            val video = videos[index]
            if (video != null) {
                Text(text = video.url, modifier = Modifier
                    .clickable {
                        val videoUrl = video.videoFiles.firstOrNull()?.link ?: return@clickable
                        navController.navigate("player?videoUrl=${Uri.encode(videoUrl)}")
                    }
                    .padding(16.dp))
            }
        }
    }
}