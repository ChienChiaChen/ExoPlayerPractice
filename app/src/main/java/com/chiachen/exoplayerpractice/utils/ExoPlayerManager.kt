package com.chiachen.exoplayerpractice.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import java.io.File

@SuppressLint("StaticFieldLeak")
object ExoPlayerManager {
    private var exoPlayer: ExoPlayer? = null
    private var currentMediaItem: MediaItem? = null

    private var context: Context? = null
    private var lifecycleOwner: LifecycleOwner? = null

    // playback state
    private var playWhenReady: Boolean = true
    private var playbackPosition: Long = 0
    private var currentMediaItemIndex: Int = 0
    private var currentVideoUrl: String? = null

    private var lifecycleObserver: LifecycleEventObserver? = null

    //region init and prepare
    fun init(context: Context, lifecycleOwner: LifecycleOwner) {
        if (ExoPlayerManager.context == null) {
            ExoPlayerManager.context = context.applicationContext
            ExoPlayerManager.lifecycleOwner = lifecycleOwner

            lifecycleObserver = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_STOP -> pause()
                    else -> {}
                }
            }
            lifecycleOwner.lifecycle.addObserver(lifecycleObserver!!)
        }

        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }
    }

    @OptIn(UnstableApi::class)
    fun preparePlayer(videoUrl: String) {
        currentVideoUrl = videoUrl
        val mediaItem = MediaItem.fromUri(
            if (videoUrl.startsWith("/")) Uri.fromFile(File(videoUrl))
            else Uri.parse(videoUrl)
        )
        currentMediaItem = mediaItem

        val cache = context?.let { CacheProvider.get(it) }

        val dataSourceFactory = CacheDataSource.Factory()
            .setCache(cache!!)
            .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory())
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)

        exoPlayer?.apply {
            setMediaSource(mediaSource)
            seekTo(currentMediaItemIndex, playbackPosition)
            playWhenReady = ExoPlayerManager.playWhenReady
            prepare()
        }
    }
    //endregion

    //region Play control
    fun play() {
        exoPlayer?.play()
        playWhenReady = true
    }

    fun pause() {
        exoPlayer?.pause()
        playWhenReady = false
    }

    fun seekTo(position: Long) {
        exoPlayer?.seekTo(position)
        playbackPosition = position
    }

    fun isPlaying(): Boolean = exoPlayer?.isPlaying ?: false
    fun getCurrentPosition(): Long = exoPlayer?.currentPosition ?: playbackPosition
    fun getPlayer(): ExoPlayer? = exoPlayer
    //endregion

    //region Save and restore Playback
    fun saveState(outState: Bundle) {
        outState.putLong("playbackPosition", exoPlayer!!.currentPosition)
        outState.putInt("currentMediaItemIndex", exoPlayer!!.currentMediaItemIndex)
        outState.putBoolean("playWhenReady", exoPlayer!!.playWhenReady)
        outState.putString("currentVideoUrl", currentVideoUrl)
    }

    fun restoreState(savedState: Bundle) {
        playbackPosition = savedState.getLong("playbackPosition", 0)
        currentMediaItemIndex = savedState.getInt("currentMediaItemIndex", 0)
        playWhenReady = savedState.getBoolean("playWhenReady", true)
        currentVideoUrl = savedState.getString("currentVideoUrl")
    }
    //endregion

    //region release
    fun release() {
        playbackPosition = 0
        currentMediaItemIndex = 0
        currentVideoUrl = null
        playWhenReady = false

        lifecycleObserver?.let {
            lifecycleOwner?.lifecycle?.removeObserver(it)
        }
        lifecycleObserver = null
        context = null
        lifecycleOwner = null
    }
    //endregion

    // Get current player state
    fun getPlayWhenReady(): Boolean = playWhenReady
    fun getPlaybackPosition(): Long = playbackPosition
    fun getCurrentMediaItemIndex(): Int = currentMediaItemIndex
}