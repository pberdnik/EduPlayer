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
import androidx.lifecycle.Transformations
import io.github.pberdnik.eduplayer.database.YoutubeDatabase
import io.github.pberdnik.eduplayer.database.asDomainModel
import io.github.pberdnik.eduplayer.domain.Playlist
import io.github.pberdnik.eduplayer.network.dto.asDatabaseModel
import io.github.pberdnik.eduplayer.network.youtubeDataApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class YoutubeRepository(private val database: YoutubeDatabase) {

    val playlists: LiveData<List<Playlist>> =
        Transformations.map(database.playlistDao.getPlaylists()) {
            it.asDomainModel()
        }

    suspend fun refreshPlaylists() {
        withContext(Dispatchers.IO) {
            val playlist = youtubeDataApiService.getPlaylistsForChannel()
            database.playlistDao.insertAll(*playlist.asDatabaseModel())
        }
    }
}
