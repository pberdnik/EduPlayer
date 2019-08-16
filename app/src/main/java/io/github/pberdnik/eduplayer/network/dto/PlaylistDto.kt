package io.github.pberdnik.eduplayer.network.dto

import io.github.pberdnik.eduplayer.database.DatabasePlaylist
import io.github.pberdnik.eduplayer.domain.Playlist


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
    val publishedAt: String,
    val title: String,
    val description: String,
    val thumbnails: Thumbnails
)

data class ContentDetails(val itemCount: Int)

/**
 * Convert Network results to database and domain objects
 */
fun NetworkPlaylists.asDomainModel(): List<Playlist> = items.map {
    Playlist(
        title = it.snippet.title,
        description = it.snippet.description,
        publishedAt = it.snippet.publishedAt,
        videosCount = it.contentDetails.itemCount
    )
}


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
