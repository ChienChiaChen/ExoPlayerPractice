package com.chiachen.exoplayerpractice.presentation.video_player

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerScreen(
    videoUrl: String,
    navController: NavController,
    viewModel: VideoPlayerViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val playerManager = remember {
        ExoPlayerManager(context, lifecycleOwner).apply {
            // Try to restore state from ViewModel
            viewModel.savedState.get<Bundle>("playerState")?.let { bundle ->
                restoreState(bundle)
            }
            initializePlayer()
            preparePlayer(videoUrl)
        }
    }

    // Save state
    DisposableEffect(Unit) {
        onDispose {
            // Update state in ViewModel
            viewModel.savedState["playerState"] =  Bundle().apply {
                playerManager.saveState(this)
            }

            playerManager.release()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Play Video") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = playerManager.getPlayer()
                    useController = true
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        )
    }
}