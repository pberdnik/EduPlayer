package io.github.pberdnik.eduplayer.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.pberdnik.eduplayer.database.localentities.DatabaseDeviceVideo
import io.github.pberdnik.eduplayer.domain.DeviceVideo

@Dao
interface LocalDao {

    @Query("SELECT * FROM device_video")
    fun getVideos(): LiveData<List<DeviceVideo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVideos(vararg video: DatabaseDeviceVideo)
}