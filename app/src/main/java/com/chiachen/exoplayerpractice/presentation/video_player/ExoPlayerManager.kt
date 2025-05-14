package com.chiachen.exoplayerpractice.presentation.video_player

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class ExoPlayerManager(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) {
    private var exoPlayer: ExoPlayer? = null
    private var currentMediaItem: MediaItem? = null
    
    // Player state
    private var playWhenReady: Boolean = true
    private var playbackPosition: Long = 0
    private var currentMediaItemIndex: Int = 0
    private var currentVideoUrl: String? = null

    private val lifecycleObserver = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_STOP -> {
                pause()
            }
            Lifecycle.Event.ON_DESTROY -> {
                savePlayerState()
                release()
            }
            else -> {}
        }
    }

    init {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
    }

    fun initializePlayer() {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }
    }

    fun preparePlayer(videoUrl: String) {
        currentVideoUrl = videoUrl
        val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
        currentMediaItem = mediaItem
        exoPlayer?.apply {
            setMediaItem(mediaItem)
            seekTo(currentMediaItemIndex, playbackPosition)
            playWhenReady = this@ExoPlayerManager.playWhenReady
            prepare()
        }
    }

    fun play() {
        exoPlayer?.play()
        playWhenReady = true
    }

    fun pause() {
        exoPlayer?.pause()
        playWhenReady = false
        savePlayerState()
    }

    fun seekTo(position: Long) {
        exoPlayer?.seekTo(position)
        playbackPosition = position
    }

    fun getCurrentPosition(): Long {
        return exoPlayer?.currentPosition ?: playbackPosition
    }

    fun isPlaying(): Boolean {
        return exoPlayer?.isPlaying ?: false
    }

    fun getPlayer(): ExoPlayer? {
        return exoPlayer
    }

    private fun savePlayerState() {
        exoPlayer?.let { player ->
            playbackPosition = player.currentPosition
            currentMediaItemIndex = player.currentMediaItemIndex
            playWhenReady = player.playWhenReady
        }
    }

    fun release() {
        savePlayerState()
        exoPlayer?.release()
        exoPlayer = null
        lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
    }

    // Get current player state
    fun getPlayWhenReady(): Boolean = playWhenReady
    fun getPlaybackPosition(): Long = playbackPosition
    fun getCurrentMediaItemIndex(): Int = currentMediaItemIndex

    // Save state to Bundle
    fun saveState(outState: Bundle) {
        savePlayerState() // Ensure latest state is saved
        outState.apply {
            putLong("playbackPosition", playbackPosition)
            putInt("currentMediaItemIndex", currentMediaItemIndex)
            putBoolean("playWhenReady", playWhenReady)
            putString("currentVideoUrl", currentVideoUrl)
        }
    }

    // Restore state from Bundle
    fun restoreState(savedState: Bundle) {
        savedState.apply {
            playbackPosition = getLong("playbackPosition", 0)
            currentMediaItemIndex = getInt("currentMediaItemIndex", 0)
            playWhenReady = getBoolean("playWhenReady", true)
            currentVideoUrl = getString("currentVideoUrl")
        }
    }
} 