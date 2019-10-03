package io.github.pberdnik.eduplayer.network.dto


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