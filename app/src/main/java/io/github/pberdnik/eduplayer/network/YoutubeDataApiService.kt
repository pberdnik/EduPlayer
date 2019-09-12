package io.github.pberdnik.eduplayer.network

import io.github.pberdnik.eduplayer.network.dto.NetworkPlaylistItems
import io.github.pberdnik.eduplayer.network.dto.NetworkPlaylists
import io.github.pberdnik.eduplayer.network.dto.NetworkVideos
import retrofit2.http.GET
import retrofit2.http.Query

val youtubeDataApiService: YoutubeDataApiService = retrofit.create(YoutubeDataApiService::class.java)

interface YoutubeDataApiService {
    @GET(
        "playlists?part=contentDetails,id,snippet&fields=etag,nextPageToken,pageInfo," +
                "items(etag,id,snippet(channelId,channelTitle,publishedAt,title,description,thumbnails),contentDetails)"
    )
    suspend fun getPlaylistsForChannel(
        @Query("channelId") chanelId: String = "UCdxpofrI-dO6oYfsqHDHphw",
        @Query("maxResults") maxResults: Int = 50
    ): NetworkPlaylists

    @GET("""playlistItems?part=contentDetails,snippet,id""")
    suspend fun getPlaylistItemsForPlaylist(
        @Query("playlistId") playlistId: String,
        @Query("maxResults") maxResults: Int = 50
    ): NetworkPlaylistItems

    @GET("""videos?part=statistics,contentDetails,snippet,id""")
    suspend fun getVideosById(
        @Query("id") videoIds: String
    ): NetworkVideos
}
