package io.github.pberdnik.eduplayer.network.dto

import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylistItem
import io.github.pberdnik.eduplayer.database.entities.DatabasePlaylistItemThumbnail
import java.util.*

data class NetworkPlaylistItems(
    val etag: String,
    val nextPageToken: String? = null,
    val pageInfo: PageInfo,
    val items: List<NetworkPlaylistItem>
)

data class NetworkPlaylistItem(
    val id: String,
    val etag: String,
    val snippet: PlaylistItemSnippet,
    val contentDetails: PlaylistItemContentDetails
)


data class PlaylistItemSnippet(
    val channelId: String,
    val channelTitle: String,
    val playlistId: String,
    val publishedAt: Date,
    val title: String,
    val description: String,
    val thumbnails: Thumbnails,
    val position: Int
)

data class PlaylistItemContentDetails(
    val videoId: String,
    val videoPublishedAt: Date
)


/*
 * Convert Network results to database and domain objects
 */

fun NetworkPlaylistItems.asDatabaseModel(): Array<DatabasePlaylistItem> = items.map {
    DatabasePlaylistItem(
        id = it.id,
        etag = it.etag,
        channelId = it.snippet.channelId,
        title = it.snippet.title,
        description = it.snippet.description,
        publishedAt = it.snippet.publishedAt,
        playlistId = it.snippet.playlistId,
        position = it.snippet.position,
        videoId = it.contentDetails.videoId
    )
}.toTypedArray()

/**
 * Extracts all thumbnails (default, medium, high, standard, maxres) from each playlistItem
 * and returns flat array of them
 */
fun NetworkPlaylistItems.asThumbnailDatabaseModel(): Array<DatabasePlaylistItemThumbnail> =
    items.flatMap { playlistItem ->
        playlistItem.snippet.thumbnails.run {
            arrayOf(default, medium, high, standard, maxres).filterNotNull().map { thumbnail ->
                DatabasePlaylistItemThumbnail(
                    id = 0,
                    playlistItemId = playlistItem.id,
                    url = thumbnail.url,
                    width = thumbnail.width,
                    height = thumbnail.height
                )
            }
        }
    }.toTypedArray()


fun NetworkPlaylistItems.videoIds(): List<String> = items.map { it.contentDetails.videoId }