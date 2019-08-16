package io.github.pberdnik.eduplayer.network

import io.github.pberdnik.eduplayer.network.dto.NetworkPlaylists
import retrofit2.http.GET
import retrofit2.http.Query

val youtubeDataApiService: YoutubeDataApiService = retrofit.create(YoutubeDataApiService::class.java)

interface YoutubeDataApiService {
    @GET(
        "playlists?part=contentDetails,id,snippet&fields=etag,nextPageToken,pageInfo," +
                "items(etag,id,snippet(publishedAt,title,description,thumbnails),contentDetails)"
    )
    suspend fun getPlaylistsForChannel(
        @Query("channelId") chanelId: String = "UCdxpofrI-dO6oYfsqHDHphw",
        @Query("maxResults") maxResults: Int = 50
    ): NetworkPlaylists
}
