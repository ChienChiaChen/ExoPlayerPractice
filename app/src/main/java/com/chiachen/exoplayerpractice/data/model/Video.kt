package com.chiachen.exoplayerpractice.data.model

import com.google.gson.annotations.SerializedName

data class Video(
    val id: Int,
    val url: String,
    val image: String,
    @SerializedName("video_files") val videoFiles: List<VideoFile>
)
