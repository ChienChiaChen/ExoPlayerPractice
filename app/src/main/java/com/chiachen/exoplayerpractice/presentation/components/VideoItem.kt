package com.chiachen.exoplayerpractice.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun VideoItem(
    videoUrl: String,
    videoImage: String,
    videoLink: String,
    isDownloaded: Boolean,
    isDownloading: Boolean,
    onVideoClick: (String) -> Unit,
    onDownloadClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onVideoClick(videoLink)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = videoImage,
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
                    text = videoUrl,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                IconButton(
                    onClick = {
                        onDownloadClick(videoLink)
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
                            strokeWidth = 2.dp,
                        )

                        else -> Icon(
                            Icons.Default.Download,
                            contentDescription = "download"
                        )
                    }
                }
            }
        }
    }
} 