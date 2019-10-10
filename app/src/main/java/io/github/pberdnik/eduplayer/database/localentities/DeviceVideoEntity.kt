package io.github.pberdnik.eduplayer.database.localentities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "device_video",
    indices = [Index("uri", unique = true)]
)
data class DatabaseDeviceVideo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val uri: String
)