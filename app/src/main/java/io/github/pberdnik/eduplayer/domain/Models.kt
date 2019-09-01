package io.github.pberdnik.eduplayer.domain

import android.text.format.DateUtils
import androidx.room.Embedded
import androidx.room.Relation
import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylistItem
import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylistItemThumbnail
import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylistThumbnail
import java.util.*

sealed class PlaylistData {
    val playlistId: String
        get() = when (this) {
            is PlaylistWithInfo -> this.playlist.id
            is PlaylistExpansion -> this.id
        }

    override fun equals(other: Any?) =
        this is PlaylistWithInfo
                && other is PlaylistWithInfo
                && (this as PlaylistWithInfo) == (other as PlaylistWithInfo)
                ||
                this is PlaylistExpansion
                && other is PlaylistExpansion
                && (this as PlaylistExpansion) == (other as PlaylistExpansion)

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

data class PlaylistWithInfo(
    @Embedded
    val playlist: Playlist,
    val expanded: Boolean = true,
    @Relation(
        entity = DatabasePlaylistThumbnail::class,
        entityColumn = "playlistId",
        parentColumn = "id"
    )
    val thumbnails: List<Thumbnail>
) : PlaylistData()

data class PlaylistExpansion(
    val id: String,
    @Relation(
        entity = DatabasePlaylistItem::class,
        entityColumn = "playlistId",
        parentColumn = "id"
    )
    val items: List<PlaylistItemWithInfo>
) : PlaylistData()

data class Playlist(
    val id: String,
    val title: String,
    val description: String,
    val publishedAt: Date,
    val videosCount: Int
) {
    val publishedAtString: String
        get() = DateUtils.getRelativeTimeSpanString(publishedAt.time, Date().time,
            0, DateUtils.FORMAT_ABBREV_RELATIVE).toString()
}

data class Thumbnail(
    val url: String,
    val width: Int,
    val height: Int
)

data class PlaylistItemWithInfo(
    val id: String,
    val title: String,
    val description: String,
    val publishedAt: Date,
    val position: Int,
    val videoId: String,
    @Relation(
        entity = DatabasePlaylistItemThumbnail::class,
        entityColumn = "playlistItemId",
        parentColumn = "id"
    )
    val thumbnails: List<Thumbnail>
)
