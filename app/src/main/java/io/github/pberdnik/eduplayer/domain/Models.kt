package io.github.pberdnik.eduplayer.domain

data class Playlist(
//    val thumbnails: List<Thumbnail>,
    val title: String,
    val description: String,
    val channelTitle: String,
    val publishedAt: String,
    val videosCount: Int
)

data class Thumbnail(
    val url: String,
    val width: Int,
    val height: Int
)
