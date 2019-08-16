package io.github.pberdnik.eduplayer.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseChannel(
    @PrimaryKey
    val id: String,
    val title: String
)