package io.github.pberdnik.eduplayer.domain

import androidx.room.Embedded

data class Playlist(
    val id: String,
    val title: String,
    val description: String,
    val channelTitle: String,
    val publishedAt: String,
    val videosCount: Int,
    @Embedded
    val thumbnails: Thumbnail
)

data class Thumbnail(
    val url: String,
    val width: Int,
    val height: Int
)
