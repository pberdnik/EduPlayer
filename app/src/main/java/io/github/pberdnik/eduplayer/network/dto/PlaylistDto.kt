package io.github.pberdnik.eduplayer.network.dto

import io.github.pberdnik.eduplayer.database.entities.DatabaseChannel
import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylist


data class NetworkPlaylists(
    val etag: String,
    val nextPageToken: String,
    val pageInfo: PageInfo,
    val items: List<NetworkPlaylist>
)

data class PageInfo(
    val totalResults: Int,
    val resultsPerPage: Int
)

data class NetworkPlaylist(
    val id: String,
    val etag: String,
    val snippet: PlaylistSnippet,
    val contentDetails: ContentDetails
)

data class PlaylistSnippet(
    val channelId: String,
    val channelTitle: String,
    val publishedAt: String,
    val title: String,
    val description: String,
    val thumbnails: Thumbnails
)

data class ContentDetails(val itemCount: Int)

/**
 * Convert Network results to database and domain objects
 */

fun NetworkPlaylists.asDatabaseModel(): Array<DatabasePlaylist> = items.map {
    DatabasePlaylist(
        id = it.id,
        etag = it.etag,
        channelId = it.snippet.channelId,
        title = it.snippet.title,
        description = it.snippet.description,
        publishedAt = it.snippet.publishedAt,
        videosCount = it.contentDetails.itemCount
    )
}.toTypedArray()

fun NetworkPlaylists.asChannelDatabaseModel(): Array<DatabaseChannel> = items.map {
    DatabaseChannel(
        id = it.snippet.channelId,
        title = it.snippet.channelTitle
    )
}.distinctBy { it.id }.toTypedArray()
