package com.chiachen.exoplayerpractice.presentation.downloaded_videos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.chiachen.exoplayerpractice.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadedVideosScreen(
    navController: NavController,
    viewModel: DownloadedVideosViewModel = viewModel()
) {
    val videoFiles by viewModel.videoFiles.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Downloaded Videos") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {
            if (videoFiles.isEmpty()) {
                item {
                    Text(
                        text = "No downloaded videos",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(videoFiles) { file ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                val videoUrl = file.absolutePath
                                navController.navigate("${Constants.VIDEO_PLAYER}?videoUrl=$videoUrl")
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = file.name,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${file.length() / (1024 * 1024)} MB",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play"
                            )
                        }
                    }
                }
            }
        }
    }
} 