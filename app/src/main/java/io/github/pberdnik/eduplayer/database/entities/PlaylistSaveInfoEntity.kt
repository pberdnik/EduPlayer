package io.github.pberdnik.eduplayer.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlist_save_info",
    foreignKeys = [ForeignKey(
        entity = DatabasePlaylist::class,
        parentColumns = ["id"],
        childColumns = ["playlistId"]
    )]
)
data class DatabasePlaylistSaveInfo(
    @PrimaryKey
    val playlistId: String,
    val learn: Boolean = false,
    val saved: Boolean = false
)

fun Array<DatabasePlaylist>.createSaveInfo(): Array<DatabasePlaylistSaveInfo> =
    this.map { DatabasePlaylistSaveInfo(playlistId = it.id) }.toTypedArray()