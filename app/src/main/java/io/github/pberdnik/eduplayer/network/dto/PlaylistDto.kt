package io.github.pberdnik.eduplayer.network.dto

import io.github.pberdnik.eduplayer.database.entities.DatabaseChannel
import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylist
import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylistThumbnail
import java.util.*


data class NetworkPlaylists(
    val etag: String,
    val nextPageToken: String? = null,
    val pageInfo: PageInfo,
    val items: List<NetworkPlaylist>
)

data class NetworkPlaylist(
    val id: String,
    val etag: String,
    val snippet: PlaylistSnippet,
    val contentDetails: PlaylistContentDetails
)

data class PlaylistSnippet(
    val channelId: String,
    val channelTitle: String,
    val publishedAt: Date,
    val title: String,
    val description: String,
    val thumbnails: Thumbnails
)

data class PlaylistContentDetails(val itemCount: Int)

/*
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

/**
 * Extracts all thumbnails (default, medium, high, standard, maxres) from each playlist
 * and returns flat array of them
 */
fun NetworkPlaylists.asThumbnailDatabaseModel(): Array<DatabasePlaylistThumbnail> = items.flatMap { playlist ->
    playlist.snippet.thumbnails.run {
        arrayOf(default, medium, high, standard, maxres).filterNotNull().map {thumbnail ->
            DatabasePlaylistThumbnail(
                id = 0,
                playlistId = playlist.id,
                url = thumbnail.url,
                width = thumbnail.width,
                height = thumbnail.height
            )
        }
    }
}.toTypedArray()