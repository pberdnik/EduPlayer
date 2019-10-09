package io.github.pberdnik.eduplayer.features.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.pberdnik.eduplayer.R
import io.github.pberdnik.eduplayer.domain.PlaylistWithInfo
import io.github.pberdnik.eduplayer.repository.YoutubeRepository
import io.github.pberdnik.eduplayer.util.Event
import io.github.pberdnik.eduplayer.util.Operation
import io.github.pberdnik.eduplayer.util.SnackbarMessages
import io.github.pberdnik.eduplayer.util.performIOOperation
import javax.inject.Inject

class ExploreViewModel @Inject constructor(private val repository: YoutubeRepository) : ViewModel() {

    val playlistsData: LiveData<List<PlaylistWithInfo>> = repository.playlists

    private val _navigateToSelectedPlaylistItem = MutableLiveData<String>()
    val navigateToSelectedPlaylistItem: LiveData<String> = _navigateToSelectedPlaylistItem

    private val _refreshStatus = MutableLiveData<Event<Operation>>()
    val refreshStatus: LiveData<Event<Operation>> = _refreshStatus

    init {
        performIOOperation(
            _refreshStatus,
            SnackbarMessages(errorMessageId = R.string.couldnt_refresh)
        ) {
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
