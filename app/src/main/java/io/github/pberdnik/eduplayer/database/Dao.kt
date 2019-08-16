package io.github.pberdnik.eduplayer.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM DatabasePlaylist")
    fun getPlaylists(): LiveData<List<DatabasePlaylist>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg playlists: DatabasePlaylist)
}