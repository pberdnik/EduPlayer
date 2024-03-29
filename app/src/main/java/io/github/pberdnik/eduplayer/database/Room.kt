package io.github.pberdnik.eduplayer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.pberdnik.eduplayer.database.entities.DatabaseChannel
import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylist
import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylistItem
import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylistItemThumbnail
import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylistSaveInfo
import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylistThumbnail
import io.github.pberdnik.eduplayer.database.entities.DatabaseUserInfo
import io.github.pberdnik.eduplayer.database.entities.DatabaseVideo
import io.github.pberdnik.eduplayer.database.entities.DatabaseVideoThumbnail
import io.github.pberdnik.eduplayer.database.entities.DateConverter
import io.github.pberdnik.eduplayer.database.entities.UriConverter
import io.github.pberdnik.eduplayer.database.localentities.DatabaseDeviceVideo


@Database(
    entities = [DatabasePlaylist::class, DatabaseChannel::class, DatabasePlaylistThumbnail::class,
        DatabasePlaylistItem::class, DatabasePlaylistItemThumbnail::class, DatabaseVideo::class,
        DatabaseVideoThumbnail::class, DatabasePlaylistSaveInfo::class, DatabaseDeviceVideo::class,
        DatabaseUserInfo::class],
    version = 21,
    exportSchema = false
)
@TypeConverters(DateConverter::class, UriConverter::class)
abstract class YoutubeDatabase : RoomDatabase() {
    abstract val playlistDao: PlaylistDao
    abstract val channelDao: ChannelDao
    abstract val thumbnailDao: ThumbnailDao
    abstract val playlistItemDao: PlaylistItemDao
    abstract val videoDao: VideoDao
    abstract val userInfoDao: UserInfoDao
    abstract val localDao: LocalDao
}
