package io.github.pberdnik.eduplayer.features.playlistdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.github.pberdnik.eduplayer.repository.YoutubeRepository
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel @AssistedInject constructor(
    private val repository: YoutubeRepository,
    @Assisted val playlistId: String
) : ViewModel() {

    val playlist = repository.getPlaylist(playlistId)
    val playlistItems = repository.getPlaylistItems(playlistId)

    init {
        viewModelScope.launch {
            repository.refreshPlaylistItems(playlistId)
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(playlistId: String): PlaylistDetailsViewModel
    }
}