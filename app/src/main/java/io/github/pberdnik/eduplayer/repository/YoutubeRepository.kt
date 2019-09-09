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

package io.github.pberdnik.eduplayer.repository

import androidx.lifecycle.LiveData
import io.github.pberdnik.eduplayer.database.YoutubeDatabase
import io.github.pberdnik.eduplayer.domain.PlaylistItemWithInfo
import io.github.pberdnik.eduplayer.domain.PlaylistWithInfo
import io.github.pberdnik.eduplayer.network.dto.asChannelDatabaseModel
import io.github.pberdnik.eduplayer.network.dto.asDatabaseModel
import io.github.pberdnik.eduplayer.network.dto.asThumbnailDatabaseModel
import io.github.pberdnik.eduplayer.network.youtubeDataApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class YoutubeRepository(private val database: YoutubeDatabase) {

    val playlists: LiveData<List<PlaylistWithInfo>> =
        database.playlistDao.getPlaylists()

    fun getPlaylistItems(playlistId: String): LiveData<List<PlaylistItemWithInfo>> =
        database.playlistItemDao.getPlaylistItemsForPlaylist(playlistId)

    fun getPlaylist(playlistId: String): LiveData<PlaylistWithInfo> =
        database.playlistDao.getPlaylist(playlistId)

    suspend fun refreshPlaylists() = withContext(Dispatchers.IO) {
        val playlists = youtubeDataApiService.getPlaylistsForChannel()
        database.channelDao.insertAll(*playlists.asChannelDatabaseModel())
        database.playlistDao.insertAll(*playlists.asDatabaseModel())
        database.thumbnailDao.insertAllPlaylistThumbnails(*playlists.asThumbnailDatabaseModel())
    }

    suspend fun refreshPlaylistItems(playlistId: String) = withContext(Dispatchers.IO) {
        val playlistItemsForPlaylist =
            youtubeDataApiService.getPlaylistItemsForPlaylist(playlistId)
        database.playlistItemDao.insertAll(*playlistItemsForPlaylist.asDatabaseModel())
        database.thumbnailDao.insertAllPlaylistItemThumbnails(
            *playlistItemsForPlaylist.asThumbnailDatabaseModel()
        )
    }

}
