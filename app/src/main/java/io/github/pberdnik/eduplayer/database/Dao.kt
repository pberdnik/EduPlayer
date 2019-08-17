package io.github.pberdnik.eduplayer.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.pberdnik.eduplayer.database.entities.DatabaseChannel
import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylist
import io.github.pberdnik.eduplayer.database.entities.DatabaseThumbnail
import io.github.pberdnik.eduplayer.domain.Playlist

@Dao
interface PlaylistDao {
    @Query(
        """
        SELECT playlists.title, description, channels.title as channelTitle, 
               publishedAt, videosCount, url, max(width) as width, height
        FROM playlists 
        INNER JOIN channels ON channels.id = playlists.channelId
        INNER JOIN thumbnails ON thumbnails.playlistId = playlists.id
        GROUP BY playlists.id"""
    )
    fun getPlaylists(): LiveData<List<Playlist>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg playlists: DatabasePlaylist)
}

@Dao
interface ChannelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg databaseChannels: DatabaseChannel)
}

@Dao
interface ThumbnailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg databaseThumbnail: DatabaseThumbnail)
}