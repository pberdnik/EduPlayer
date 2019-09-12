package io.github.pberdnik.eduplayer.domain

import android.text.format.DateUtils
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylistItemThumbnail
import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylistThumbnail
import io.github.pberdnik.eduplayer.database.entities.DatabaseVideo
import java.util.*

data class PlaylistWithInfo(
    @Embedded
    val playlist: Playlist,
    @Relation(
        entity = DatabasePlaylistThumbnail::class,
        entityColumn = "playlistId",
        parentColumn = "id"
    )
    val thumbnails: List<Thumbnail>
)

data class Playlist(
    val id: String,
    val title: String,
    val description: String,
    val publishedAt: Date,
    val videosCount: Int
) {
    val publishedAtString: String
        get() = DateUtils.getRelativeTimeSpanString(
            publishedAt.time, Date().time,
            0, DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString()
}

data class Thumbnail(
    val url: String,
    val width: Int,
    val height: Int
)

data class Video(
    val duration: String
)

data class PlaylistItemWithInfo(
    val id: String,
    val title: String,
    val description: String,
    val publishedAt: Date,
    val position: Int,
    val videoId: String,
    @Relation(
        entity = DatabaseVideo::class,
        entityColumn = "id",
        parentColumn = "videoId"
    )
    val videos: List<Video>,
    @Relation(
        entity = DatabasePlaylistItemThumbnail::class,
        entityColumn = "playlistItemId",
        parentColumn = "id"
    )
    val thumbnails: List<Thumbnail>
) {
    @Ignore
    var duration: String = convert(videos[0].duration)

    /* Converts duration in youtube format to human readable:
    *  PT32S -> 0:32, PT1H2M -> 1:02:00, PT1H1M1S -> 1:01:01 */
    private fun convert(youtubeDuration: String): String {
        var h = 0
        var m = 0
        var s = 0
        youtubeDuration.windowed(size = 3, step = 1).forEach { str ->
            when (str.last()) {
                'H' -> h = str.filter(Char::isDigit).toInt()
                'M' -> m = str.filter(Char::isDigit).toInt()
                'S' -> s = str.filter(Char::isDigit).toInt()
            }
        }
        return if (h == 0) {
            String.format("%d:%02d", m, s)
        } else {
            String.format("%d:%02d:%02d", h, m, s)
        }
    }
}

data class VideoWithInfo(
    val id: String,
    val title: String,
    val description: String,
    val publishedAt: Date,
    val duration: String,
    val viewCount: Long,
    val likeCount: Long = 0,
    val dislikeCount: Long = 0,
    @Relation(
        entity = DatabasePlaylistItemThumbnail::class,
        entityColumn = "playlistItemId",
        parentColumn = "id"
    )
    val thumbnails: List<Thumbnail>
)
