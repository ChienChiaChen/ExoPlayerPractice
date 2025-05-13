package com.chiachen.exoplayerpractice.presentation.video_list

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoListScreen(
    navController: NavController,
    viewModel: VideoListViewModel = viewModel()
) {
    val videos = viewModel.videos.collectAsLazyPagingItems()
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
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                val videoUrl =
                                    video.videoFiles.firstOrNull()?.link ?: return@clickable
                                navController.navigate("player?videoUrl=${Uri.encode(videoUrl)}")
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
                            Text(
                                text = video.url,
                                modifier = Modifier.padding(8.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}