package io.github.pberdnik.eduplayer.feat.explore

import android.app.Application
import androidx.lifecycle.*
import io.github.pberdnik.eduplayer.database.getDatabase
import io.github.pberdnik.eduplayer.domain.PlaylistWithInfo
import io.github.pberdnik.eduplayer.repository.YoutubeRepository
import kotlinx.coroutines.launch

class ExploreViewModel(app: Application) : AndroidViewModel(app) {

    private val database = getDatabase(app)
    private val repository = YoutubeRepository(database)

    val playlistsData: LiveData<List<PlaylistWithInfo>> = repository.playlists

    private val _navigateToSelectedPlaylistItem = MutableLiveData<String>()
    val navigateToSelectedPlaylistItem: LiveData<String> = _navigateToSelectedPlaylistItem

    init {
        viewModelScope.launch {
            repository.refreshPlaylists()
        }
    }

    fun switchPlaylistExpansion(playlistWithInfo: PlaylistWithInfo) {
        viewModelScope.launch {
            repository.refreshPlaylistItems(playlistWithInfo.playlist.id)
        }
    }

    fun displayPlaylistDetails(playlistId: String) {
        _navigateToSelectedPlaylistItem.value = playlistId
    }

    fun displayPlaylistDetailsComplete() {
        _navigateToSelectedPlaylistItem.value = null
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExploreViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ExploreViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }
}
