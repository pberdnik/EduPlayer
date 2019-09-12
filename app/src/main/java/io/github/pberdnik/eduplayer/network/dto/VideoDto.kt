package io.github.pberdnik.eduplayer.network.dto

import io.github.pberdnik.eduplayer.database.entities.DatabaseVideo
import io.github.pberdnik.eduplayer.database.entities.DatabaseVideoThumbnail
import java.util.*

data class NetworkVideos(
    val etag: String,
    val nextPageToken: String? = null,
    val pageInfo: PageInfo,
    val items: List<NetworkVideo>
)

data class NetworkVideo(
    val id: String,
    val etag: String,
    val snippet: VideoSnippet,
    val contentDetails: VideoContentDetails,
    val statistics: VideoStatistics
)


data class VideoSnippet(
    val channelId: String,
    val channelTitle: String,
    val publishedAt: Date,
    val title: String,
    val description: String,
    val thumbnails: Thumbnails
)

data class VideoContentDetails(
    val duration: String
)

data class VideoStatistics(
    val viewCount: Long,
    val likeCount: Long = 0,
    val dislikeCount: Long = 0
)


/*
 * Convert Network results to database and domain objects
 */

fun NetworkVideos.asDatabaseModel(): Array<DatabaseVideo> = items.map {
    DatabaseVideo(
        id = it.id,
        etag = it.etag,
        channelId = it.snippet.channelId,
        title = it.snippet.title,
        description = it.snippet.description,
        publishedAt = it.snippet.publishedAt,
        duration = it.contentDetails.duration,
        viewCount = it.statistics.viewCount,
        likeCount = it.statistics.likeCount,
        dislikeCount = it.statistics.dislikeCount
    )
}.toTypedArray()

/**
 * Extracts all thumbnails (default, medium, high, standard, maxres) from each video
 * and returns flat array of them
 */
fun NetworkVideos.asThumbnailDatabaseModel(): Array<DatabaseVideoThumbnail> =
    items.flatMap { video ->
        video.snippet.thumbnails.run {
            arrayOf(default, medium, high, standard, maxres).filterNotNull().map { thumbnail ->
                DatabaseVideoThumbnail(
                    id = 0,
                    videoId = video.id,
                    url = thumbnail.url,
                    width = thumbnail.width,
                    height = thumbnail.height
                )
            }
        }
    }.toTypedArray()