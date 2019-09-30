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

import android.text.TextUtils
import androidx.lifecycle.LiveData
import dagger.Reusable
import io.github.pberdnik.eduplayer.database.ChannelDao
import io.github.pberdnik.eduplayer.database.PlaylistDao
import io.github.pberdnik.eduplayer.database.PlaylistItemDao
import io.github.pberdnik.eduplayer.database.ThumbnailDao
import io.github.pberdnik.eduplayer.database.VideoDao
import io.github.pberdnik.eduplayer.domain.PlaylistItemWithInfo
import io.github.pberdnik.eduplayer.domain.PlaylistWithInfo
import io.github.pberdnik.eduplayer.network.YoutubeDataApiService
import io.github.pberdnik.eduplayer.network.dto.asChannelDatabaseModel
import io.github.pberdnik.eduplayer.network.dto.asDatabaseModel
import io.github.pberdnik.eduplayer.network.dto.asThumbnailDatabaseModel
import io.github.pberdnik.eduplayer.network.dto.videoIds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Reusable
class YoutubeRepository @Inject constructor(
    private val youtubeDataApiService: YoutubeDataApiService,
    private val playlistDao: PlaylistDao,
    private val channelDao: ChannelDao,
    private val thumbnailDao: ThumbnailDao,
    private val playlistItemDao: PlaylistItemDao,
    private val videoDao: VideoDao
) {

    val playlists: LiveData<List<PlaylistWithInfo>> =
        playlistDao.getPlaylists()

    fun getPlaylistItems(playlistId: String): LiveData<List<PlaylistItemWithInfo>> =
        playlistItemDao.getPlaylistItemsForPlaylist(playlistId)

    fun getPlaylist(playlistId: String): LiveData<PlaylistWithInfo> =
        playlistDao.getPlaylist(playlistId)

    suspend fun refreshPlaylists() = withContext(Dispatchers.IO) {
        val playlists = youtubeDataApiService.getPlaylistsForChannel()
        channelDao.insertAll(*playlists.asChannelDatabaseModel())
        playlistDao.insertAll(*playlists.asDatabaseModel())
        thumbnailDao.insertAllPlaylistThumbnails(*playlists.asThumbnailDatabaseModel())
    }

    suspend fun refreshPlaylistItems(playlistId: String) = withContext(Dispatchers.IO) {
        val playlistItemsForPlaylist =
            youtubeDataApiService.getPlaylistItemsForPlaylist(playlistId)
        val videosById = youtubeDataApiService
                .getVideosById(TextUtils.join(",", playlistItemsForPlaylist.videoIds()))
        videoDao.insertAll(*videosById.asDatabaseModel())
        thumbnailDao.insertAllVideoThumbnails(*videosById.asThumbnailDatabaseModel())
        playlistItemDao.insertAll(*playlistItemsForPlaylist.asDatabaseModel())
        thumbnailDao.insertAllPlaylistItemThumbnails(
            *playlistItemsForPlaylist.asThumbnailDatabaseModel()
        )
    }

}
