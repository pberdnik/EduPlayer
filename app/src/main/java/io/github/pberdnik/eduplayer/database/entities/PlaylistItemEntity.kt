package io.github.pberdnik.eduplayer.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "playlist_items",
    foreignKeys = [
        ForeignKey(
            entity = DatabaseChannel::class,
            parentColumns = ["id"],
            childColumns = ["channelId"]
        ), ForeignKey(
            entity = DatabasePlaylist::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"]
        )]
)
data class DatabasePlaylistItem constructor(
    @PrimaryKey
    val id: String,
    val etag: String,
    val channelId: String,
    val playlistId: String,
    val title: String,
    val description: String,
    val publishedAt: Date,
    val position: Int,
    val videoId: String
)