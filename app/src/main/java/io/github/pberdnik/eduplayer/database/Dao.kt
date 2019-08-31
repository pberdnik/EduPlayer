package io.github.pberdnik.eduplayer.database

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.pberdnik.eduplayer.database.entities.*
import io.github.pberdnik.eduplayer.domain.PlaylistItemWithInfo
import io.github.pberdnik.eduplayer.domain.PlaylistWithInfo
import io.github.pberdnik.eduplayer.domain.PlaylistExpansion

@Dao
interface PlaylistDao {
    @Transaction
    @Query("SELECT * FROM playlists")
    fun getPlaylists(): LiveData<List<PlaylistWithInfo>>

    @Transaction
    @Query("SELECT * FROM playlists WHERE expanded = 1")
    fun getExpandedPlaylistsItems(): LiveData<List<PlaylistExpansion>>

    @Transaction
    @Query("SELECT * FROM playlists WHERE expanded = 1")
    fun getExpandedPlaylistsItemsNoLD(): List<PlaylistExpansion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg playlists: DatabasePlaylist)

    @Query("UPDATE playlists SET expanded = not expanded WHERE id = :playlistId")
    suspend fun switchExpansion(playlistId: String)
}

@Dao
interface PlaylistItemDao {
    @Query(
        """
        SELECT pi.id, pi.title, description, c.title as channelTitle, 
               publishedAt, position, videoId, url, max(width) as width, height
        FROM playlist_items as pi
        INNER JOIN channels as c ON c.id = pi.channelId
        INNER JOIN playlist_item_thumbnails  as pit ON pit.playlistItemId = pi.id
        WHERE pi.id = :playlistId
    """
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
}