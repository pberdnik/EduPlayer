/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.github.pberdnik.eduplayer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.pberdnik.eduplayer.database.entities.*


@Database(
    entities = [DatabasePlaylist::class, DatabaseChannel::class, DatabasePlaylistThumbnail::class,
        DatabasePlaylistItem::class, DatabasePlaylistItemThumbnail::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class YoutubeDatabase : RoomDatabase() {
    abstract val playlistDao: PlaylistDao
    abstract val channelDao: ChannelDao
    abstract val thumbnailDao: ThumbnailDao
    abstract val playlistItemDao: PlaylistItemDao
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
