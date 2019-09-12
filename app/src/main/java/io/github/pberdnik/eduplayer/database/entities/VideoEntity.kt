package io.github.pberdnik.eduplayer.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "videos"
)
data class DatabaseVideo constructor(
    @PrimaryKey
    val id: String,
    val etag: String,
    val channelId: String,
    val title: String,
    val description: String,
    val publishedAt: Date,
    val duration: String,
    val viewCount: Long,
    val likeCount: Long,
    val dislikeCount: Long
)