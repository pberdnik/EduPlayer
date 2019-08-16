package io.github.pberdnik.eduplayer.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = DatabaseChannel::class,
        parentColumns = ["id"],
        childColumns = ["channelId"]
    )]
)
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
