package io.github.pberdnik.eduplayer.database

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.pberdnik.eduplayer.database.entities.*
import io.github.pberdnik.eduplayer.domain.PlaylistItemWithInfo
import io.github.pberdnik.eduplayer.domain.PlaylistWithInfo
import io.github.pberdnik.eduplayer.domain.VideoWithInfo


@Dao
interface PlaylistDao {
    @Transaction
    @Query("SELECT * FROM playlists")
    fun getPlaylists(): LiveData<List<PlaylistWithInfo>>

    @Transaction
    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getPlaylist(playlistId: String): LiveData<PlaylistWithInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg playlists: DatabasePlaylist)
}


@Dao
interface PlaylistItemDao {
    @Query(
        """
        SELECT * FROM playlist_items as pi 
        WHERE pi.playlistId = :playlistId
        ORDER BY pi.position"""
    )
    fun getPlaylistItemsForPlaylist(playlistId: String): LiveData<List<PlaylistItemWithInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg playlistItems: DatabasePlaylistItem)
}


@Dao
interface ChannelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg databaseChannels: DatabaseChannel)
}


@Dao
interface ThumbnailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPlaylistThumbnails(vararg databaseThumbnail: DatabasePlaylistThumbnail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPlaylistItemThumbnails(vararg databaseThumbnail: DatabasePlaylistItemThumbnail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllVideoThumbnails(vararg databaseThumbnail: DatabaseVideoThumbnail)
}


@Dao
interface VideoDao {
    @Transaction
    @Query(
        """
        SELECT * FROM videos as v 
        WHERE v.id IN (:videoIds)"""
    )
    fun getVideosById(videoIds: List<String>): LiveData<List<VideoWithInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg videos: DatabaseVideo)
}