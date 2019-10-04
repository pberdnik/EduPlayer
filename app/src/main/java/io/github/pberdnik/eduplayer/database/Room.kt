package io.github.pberdnik.eduplayer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.pberdnik.eduplayer.database.entities.DatabaseChannel
import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylist
import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylistItem
import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylistItemThumbnail
import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylistThumbnail
import io.github.pberdnik.eduplayer.database.entities.DatabaseVideo
import io.github.pberdnik.eduplayer.database.entities.DatabaseVideoThumbnail
import io.github.pberdnik.eduplayer.database.entities.DateConverter


@Database(
    entities = [DatabasePlaylist::class, DatabaseChannel::class, DatabasePlaylistThumbnail::class,
        DatabasePlaylistItem::class, DatabasePlaylistItemThumbnail::class, DatabaseVideo::class,
        DatabaseVideoThumbnail::class],
    version = 9,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class YoutubeDatabase : RoomDatabase() {
    abstract val playlistDao: PlaylistDao
    abstract val channelDao: ChannelDao
    abstract val thumbnailDao: ThumbnailDao
    abstract val playlistItemDao: PlaylistItemDao
    abstract val videoDao: VideoDao
}
