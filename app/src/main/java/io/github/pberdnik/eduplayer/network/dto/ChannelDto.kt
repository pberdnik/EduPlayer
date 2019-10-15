package io.github.pberdnik.eduplayer.network.dto

import io.github.pberdnik.eduplayer.database.entities.DatabaseUserInfo


data class NetworkChannels(
    val etag: String,
    val items: List<NetworkChannel>
)

data class NetworkChannel(
    val etag: String,
    val snippet: ChannelSnippet
)

data class ChannelSnippet(
    val title: String,
    val thumbnails: Thumbnails
)


fun NetworkChannels.asDatabaseUserInfo(selectedAccountName: String) =
    DatabaseUserInfo(
        accountName = selectedAccountName,
        avatarUrl = this.items.getOrNull(0)?.snippet?.thumbnails?.best,
        accountDescription = this.items.getOrNull(0)?.snippet?.title
    )