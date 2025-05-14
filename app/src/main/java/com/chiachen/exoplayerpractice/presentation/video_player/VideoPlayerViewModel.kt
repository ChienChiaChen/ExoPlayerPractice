package com.chiachen.exoplayerpractice.presentation.video_player

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.media3.exoplayer.ExoPlayer

class VideoPlayerViewModel : ViewModel() {
    var playWhenReady by mutableStateOf(true)
        private set

    var playbackPosition by mutableLongStateOf(0L)
        private set

    var currentMediaItemIndex by mutableIntStateOf(0)
        private set

    fun savePlayerState(player: ExoPlayer) {
        playbackPosition = player.currentPosition
        currentMediaItemIndex = player.currentMediaItemIndex
        playWhenReady = player.playWhenReady
    }
}