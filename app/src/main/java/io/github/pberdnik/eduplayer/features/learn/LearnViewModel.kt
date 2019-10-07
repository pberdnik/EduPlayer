package io.github.pberdnik.eduplayer.features.learn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.pberdnik.eduplayer.domain.PlaylistWithInfo
import io.github.pberdnik.eduplayer.repository.YoutubeRepository
import io.github.pberdnik.eduplayer.util.OperationStatus
import javax.inject.Inject


class LearnViewModel @Inject constructor(private val repository: YoutubeRepository) :
    ViewModel() {

    val operationStatus = MutableLiveData<OperationStatus>()

    val playlistsData: LiveData<List<PlaylistWithInfo>> = repository.learnPlaylists

    private val _navigateToSelectedPlaylistItem = MutableLiveData<String>()
    val navigateToSelectedPlaylistItem: LiveData<String> = _navigateToSelectedPlaylistItem

    fun displayPlaylistDetails(playlistId: String) {
        _navigateToSelectedPlaylistItem.value = playlistId
    }

    fun displayPlaylistDetailsComplete() {
        _navigateToSelectedPlaylistItem.value = null
    }
}
