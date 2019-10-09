package io.github.pberdnik.eduplayer.features.library.youtubeplaylists

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

class YoutubePlaylistsViewModel @Inject constructor(
    private val repository: YoutubeRepository
) : ViewModel() {

    private val _refreshStatus = MutableLiveData<Event<Operation>>()
    val refreshStatus: LiveData<Event<Operation>> = _refreshStatus

    val playlistsData: LiveData<List<PlaylistWithInfo>> = repository.myPlaylists

    private val _navigateToSelectedPlaylistItem = MutableLiveData<Event<String>>()
    val navigateToSelectedPlaylistItem: LiveData<Event<String>> = _navigateToSelectedPlaylistItem

    init {
        performIOOperation(
            _refreshStatus,
            SnackbarMessages(errorMessageId = R.string.couldnt_refresh)
        ) {
            repository.refreshMyPlaylists()
        }
    }

    fun displayPlaylistDetails(playlistId: String) {
        _navigateToSelectedPlaylistItem.value = Event(playlistId)
    }
}
