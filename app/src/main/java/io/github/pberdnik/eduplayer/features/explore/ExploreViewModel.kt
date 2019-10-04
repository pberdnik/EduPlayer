package io.github.pberdnik.eduplayer.features.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.pberdnik.eduplayer.domain.PlaylistWithInfo
import io.github.pberdnik.eduplayer.repository.YoutubeRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExploreViewModel @Inject constructor(private val repository: YoutubeRepository) : ViewModel() {

    val playlistsData: LiveData<List<PlaylistWithInfo>> = repository.playlists

    private val _navigateToSelectedPlaylistItem = MutableLiveData<String>()
    val navigateToSelectedPlaylistItem: LiveData<String> = _navigateToSelectedPlaylistItem

    init {
        viewModelScope.launch {
            repository.refreshPlaylistsForChannel()
        }
    }

    fun displayPlaylistDetails(playlistId: String) {
        _navigateToSelectedPlaylistItem.value = playlistId
    }

    fun displayPlaylistDetailsComplete() {
        _navigateToSelectedPlaylistItem.value = null
    }
}
