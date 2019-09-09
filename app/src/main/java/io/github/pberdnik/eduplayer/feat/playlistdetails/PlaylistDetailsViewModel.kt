package io.github.pberdnik.eduplayer.feat.playlistdetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.pberdnik.eduplayer.database.getDatabase
import io.github.pberdnik.eduplayer.repository.YoutubeRepository
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(val playlistId: String, app: Application)
    : AndroidViewModel(app) {

    private val database = getDatabase(app)
    private val repository = YoutubeRepository(database)

    val playlist = repository.getPlaylist(playlistId)
    val playlistItems = repository.getPlaylistItems(playlistId)

    init {
        viewModelScope.launch {
            repository.refreshPlaylistItems(playlistId)
        }
    }

    class Factory(val playlistId: String, val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlaylistDetailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PlaylistDetailsViewModel(playlistId, app) as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }
}