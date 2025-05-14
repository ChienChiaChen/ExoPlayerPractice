package com.chiachen.exoplayerpractice.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.chiachen.exoplayerpractice.data.model.Video
import com.chiachen.exoplayerpractice.utils.DownloadHelper

@Composable
fun VideoItem(
    video: Video,
    isDownloading: Boolean,
    onVideoClick: (String) -> Unit,
    onDownloadClick: (String) -> Unit
) {
    val fileName = remember(video.id) { "video_${video.id}.mp4" }
    val isDownloaded = remember(fileName) { DownloadHelper.isVideoDownloaded(fileName) }
    val videoUrl = remember(video) { video.videoFiles.firstOrNull()?.link }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { videoUrl?.let { onVideoClick(it) } },
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
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
                    onClick = { videoUrl?.let { onDownloadClick(it) } },
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
                            contentDescription = "Download"
                        )
                    }
                }
            }
        }
    }
} 