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
import io.github.pberdnik.eduplayer.network.dto.NetworkPlaylists
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
        playlistDao.getPlaylistsForChannel()

    val myPlaylists: LiveData<List<PlaylistWithInfo>> =
        playlistDao.getMyPlaylists()

    val eduPlayerPlaylists: LiveData<List<PlaylistWithInfo>> =
        playlistDao.getEduPlayerPlaylists()

    val learnPlaylists: LiveData<List<PlaylistWithInfo>> =
        playlistDao.getLearnPlaylists()

    fun getPlaylistItems(playlistId: String): LiveData<List<PlaylistItemWithInfo>> =
        playlistItemDao.getPlaylistItemsForPlaylist(playlistId)

    fun getPlaylist(playlistId: String): LiveData<PlaylistWithInfo> =
        playlistDao.getPlaylist(playlistId)

    suspend fun refreshPlaylistsForChannel() = withContext(Dispatchers.IO) {
        val playlists = youtubeDataApiService.getPlaylistsForChannel()
        insertPlaylists(playlists)
    }

    suspend fun refreshMyPlaylists() = withContext(Dispatchers.IO) {
        val playlists = youtubeDataApiService.getMyPlaylists()
        insertPlaylists(playlists, mine = true)
    }

    private suspend fun insertPlaylists(playlists: NetworkPlaylists, mine: Boolean = false) {
        channelDao.insertAll(*playlists.asChannelDatabaseModel())
        val playlistsDM = playlists.asDatabaseModel()
        if (mine) playlistsDM.forEach { it.mine = true }
        playlistDao.insertPlaylistsWithSaveInfo(playlistsDM)
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

    suspend fun changePlaylistSavedState(playlistId: String) {
        playlistDao.changeSavedState(playlistId)
    }

    suspend fun changePlaylistLearnState(playlistId: String) {
        playlistDao.changeLearnState(playlistId)
    }

    suspend fun getSaveInfo(playlistId: String) = playlistDao.getSaveInfo(playlistId)

}
