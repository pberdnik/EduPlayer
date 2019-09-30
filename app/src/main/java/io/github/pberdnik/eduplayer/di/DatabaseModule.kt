package io.github.pberdnik.eduplayer.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.pberdnik.eduplayer.database.ChannelDao
import io.github.pberdnik.eduplayer.database.PlaylistDao
import io.github.pberdnik.eduplayer.database.PlaylistItemDao
import io.github.pberdnik.eduplayer.database.ThumbnailDao
import io.github.pberdnik.eduplayer.database.VideoDao
import io.github.pberdnik.eduplayer.database.YoutubeDatabase
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Singleton
    @JvmStatic
    @Provides
    fun provideDatabase(context: Context): YoutubeDatabase =
        Room.databaseBuilder(
            context,
            YoutubeDatabase::class.java,
            "youtube.db"
        ).fallbackToDestructiveMigration().build()

    @Reusable
    @JvmStatic
    @Provides
    fun providePlaylistDao(db: YoutubeDatabase): PlaylistDao = db.playlistDao

    @Reusable
    @JvmStatic
    @Provides
    fun provideChannelDao(db: YoutubeDatabase): ChannelDao = db.channelDao

    @Reusable
    @JvmStatic
    @Provides
    fun provideThumbnailDao(db: YoutubeDatabase): ThumbnailDao = db.thumbnailDao

    @Reusable
    @JvmStatic
    @Provides
    fun providePlaylistItemDao(db: YoutubeDatabase): PlaylistItemDao = db.playlistItemDao

    @Reusable
    @JvmStatic
    @Provides
    fun provideVideoDao(db: YoutubeDatabase): VideoDao = db.videoDao
}