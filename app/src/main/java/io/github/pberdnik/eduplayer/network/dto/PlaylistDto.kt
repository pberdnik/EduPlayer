package io.github.pberdnik.eduplayer.network.dto


data class Playlists(
    val etag: String,
    val nextPageToken: String,
    val pageInfo: PageInfo,
    val items: List<Playlist>
)

data class PageInfo(
    val totalResults: Int,
    val resultsPerPage: Int
)

data class Playlist(
    val id: String,
    val etag: String,
    val snippet: PlaylistSnippet,
    val contentDetails: ContentDetails
)

data class PlaylistSnippet(
    val publishedAt: String,
    val title: String,
    val description: String,
    val thumbnails: Thumbnails
)

data class ContentDetails(val itemCount: Int)