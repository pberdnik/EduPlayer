package io.github.pberdnik.eduplayer.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.pberdnik.eduplayer.database.entities.*
import io.github.pberdnik.eduplayer.domain.Playlist
import io.github.pberdnik.eduplayer.domain.PlaylistItem

@Dao
interface PlaylistDao {
    @Query(
        """
        SELECT playlists.id, playlists.title, description, channels.title as channelTitle, 
               publishedAt, videosCount, url, max(width) as width, height
        FROM playlists 
        INNER JOIN channels ON channels.id = playlists.channelId
        INNER JOIN playlist_thumbnails ON playlist_thumbnails.playlistId = playlists.id
        GROUP BY playlists.id"""
    )
    fun getPlaylists(): LiveData<List<Playlist>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg playlists: DatabasePlaylist)
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
    fun getPlaylistItemsForPlaylist(playlistId: String): LiveData<List<PlaylistItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg playlistItems: DatabasePlaylistItem)
}

@Dao
interface ChannelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg databaseChannels: DatabaseChannel)
}

@Dao
interface ThumbnailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPlaylistThumbnails(vararg databaseThumbnail: DatabasePlaylistThumbnail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPlaylistItemThumbnails(vararg databaseThumbnail: DatabasePlaylistItemThumbnail)
}