package io.github.pberdnik.eduplayer.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
data class DatabaseUserInfo(
    @PrimaryKey
    val accountName: String,
    val avatarUrl: String?,
    val accountDescription: String?
)