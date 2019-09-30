package io.github.pberdnik.eduplayer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.pberdnik.eduplayer.database.entities.*


@Database(
    entities = [DatabasePlaylist::class, DatabaseChannel::class, DatabasePlaylistThumbnail::class,
        DatabasePlaylistItem::class, DatabasePlaylistItemThumbnail::class, DatabaseVideo::class,
        DatabaseVideoThumbnail::class],
    version = 8,
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

private lateinit var INSTANCE: YoutubeDatabase

fun getDatabase(context: Context): YoutubeDatabase {
    synchronized(YoutubeDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                YoutubeDatabase::class.java,
                "youtube.db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}
