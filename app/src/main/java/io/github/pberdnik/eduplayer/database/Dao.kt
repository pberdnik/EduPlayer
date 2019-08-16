package io.github.pberdnik.eduplayer.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.pberdnik.eduplayer.database.entities.DatabaseChannel
import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylist
import io.github.pberdnik.eduplayer.domain.Playlist

@Dao
interface PlaylistDao {
    @Query("SELECT DatabasePlaylist.title, description, DatabaseChannel.title as channelTitle, publishedAt, videosCount FROM DatabasePlaylist INNER JOIN DatabaseChannel ON DatabaseChannel.id = DatabasePlaylist.channelId")
    fun getPlaylists(): LiveData<List<Playlist>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg playlists: DatabasePlaylist)
}
@Dao
interface ChannelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg databaseChannels: DatabaseChannel)
}