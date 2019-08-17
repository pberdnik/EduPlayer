package io.github.pberdnik.eduplayer.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "thumbnails",
    foreignKeys = [ForeignKey(
        entity = DatabasePlaylist::class,
        parentColumns = ["id"],
        childColumns = ["playlistId"]
    )]
)
data class DatabaseThumbnail(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val playlistId: String,
    val url: String,
    val width: Int,
    val height: Int
)