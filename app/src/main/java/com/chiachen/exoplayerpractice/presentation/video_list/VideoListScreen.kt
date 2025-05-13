package com.chiachen.exoplayerpractice.presentation.video_list

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun VideoListScreen(viewModel: VideoListViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val videos = viewModel.videos.collectAsLazyPagingItems()

    LazyColumn {
        items(
            count = videos.itemCount,
            key = { index -> videos[index]?.id ?: index }
        ) { index ->
            val video = videos[index]
            if (video != null) {
                Text(text = video.url, modifier = Modifier.padding(16.dp))
            }
        }
    }
}