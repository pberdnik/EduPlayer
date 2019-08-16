package io.github.pberdnik.eduplayer.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.pberdnik.eduplayer.domain.Playlist

@Entity
data class DatabasePlaylist constructor(
    @PrimaryKey
    val id: String,
    val etag: String,
    val channelId: String,
    val title: String,
    val description: String,
    val publishedAt: String,
    val videosCount: Int
)

fun List<DatabasePlaylist>.asDomainModel(): List<Playlist> = map {
    Playlist(
        title = it.title,
        description = it.description,
        publishedAt = it.publishedAt,
        videosCount = it.videosCount
    )
}
