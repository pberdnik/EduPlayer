package io.github.pberdnik.eduplayer.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlist_thumbnails",
    foreignKeys = [ForeignKey(
        entity = DatabasePlaylist::class,
        parentColumns = ["id"],
        childColumns = ["playlistId"]
    )],
    indices = [Index("url", unique = true)]
)
data class DatabasePlaylistThumbnail(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val playlistId: String,
    val url: String,
    val width: Int,
    val height: Int
)


@Entity(
    tableName = "playlist_item_thumbnails",
    foreignKeys = [ForeignKey(
        entity = DatabasePlaylistItem::class,
        parentColumns = ["id"],
        childColumns = ["playlistItemId"]
    )],
    indices = [Index("url", unique = true)]
)
data class DatabasePlaylistItemThumbnail(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val playlistItemId: String,
    val url: String,
    val width: Int,
    val height: Int
)


@Entity(
    tableName = "video_thumbnails",
    foreignKeys = [ForeignKey(
        entity = DatabaseVideo::class,
        parentColumns = ["id"],
        childColumns = ["videoId"]
    )],
    indices = [Index("url", unique = true)]
)
data class DatabaseVideoThumbnail(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val videoId: String,
    val url: String,
    val width: Int,
    val height: Int
)