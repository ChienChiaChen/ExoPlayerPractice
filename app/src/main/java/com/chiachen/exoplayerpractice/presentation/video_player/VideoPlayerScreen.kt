package com.chiachen.exoplayerpractice.presentation.video_player

import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.chiachen.exoplayerpractice.utils.ExoPlayerManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerScreen(
    videoUrl: String,
    navController: NavController,
    viewModel: VideoPlayerViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val playerReady = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        ExoPlayerManager.init(context, lifecycleOwner)
        viewModel.savedState.get<Bundle>("playerState")?.let {
            ExoPlayerManager.restoreState(it)
        }
        ExoPlayerManager.preparePlayer(videoUrl)
        playerReady.value = true
    }

    DisposableEffect(Unit) {
        onDispose {
            val bundle = Bundle()
            ExoPlayerManager.saveState(bundle)
            viewModel.savedState["playerState"] = bundle
            ExoPlayerManager.release()
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
        if (playerReady.value) {
            AndroidView(
                factory = {
                    PlayerView(it).apply {
                        player = ExoPlayerManager.getPlayer()
                        useController = true
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}