package io.github.pberdnik.eduplayer.features.playlistdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.github.pberdnik.eduplayer.R
import io.github.pberdnik.eduplayer.repository.YoutubeRepository
import io.github.pberdnik.eduplayer.util.Event
import io.github.pberdnik.eduplayer.util.Operation
import io.github.pberdnik.eduplayer.util.SnackbarMessages
import io.github.pberdnik.eduplayer.util.performIOOperation

private val addedLearnSnackbarMessages = SnackbarMessages(
    doneMessageId = R.string.added_to_learn,
    errorMessageId = R.string.couldnt_add_to_learn
)
private val removedLearnSnackbarMessages = SnackbarMessages(
    doneMessageId = R.string.removed_from_learn,
    errorMessageId = R.string.couldnt_add_to_learn
)
val addedLibrarySnackbarMessages = SnackbarMessages(
    doneMessageId = R.string.added_to_library,
    errorMessageId = R.string.couldnt_add_to_library
)
val removedLibrarySnackbarMessages = SnackbarMessages(
    doneMessageId = R.string.removed_from_library,
    errorMessageId = R.string.couldnt_add_to_library
)

class PlaylistDetailsViewModel @AssistedInject constructor(
    private val repository: YoutubeRepository,
    @Assisted val playlistId: String
) : ViewModel() {

    val playlist = repository.getPlaylist(playlistId)
    val playlistItems = repository.getPlaylistItems(playlistId)

    private val _refreshStatus = MutableLiveData<Event<Operation>>()
    val refreshStatus: LiveData<Event<Operation>> = _refreshStatus

    private var isAddedToLearn = false
    private val _learnAddStatus = MutableLiveData<Event<Operation>>()
    val learnAddStatus: LiveData<Event<Operation>> = _learnAddStatus

    private var isAddedToLibrary = false
    private val _libraryAddStatus = MutableLiveData<Event<Operation>>()
    val libraryAddStatus: LiveData<Event<Operation>> = _libraryAddStatus

    init {
        performIOOperation(
            _refreshStatus,
            SnackbarMessages(errorMessageId = R.string.couldnt_refresh_playlist)
        ) {
            repository.refreshPlaylistItems(playlistId)
        }
        performIOOperation {
            val (lib, lrn) = repository.getSaveInfo(playlistId)
            isAddedToLibrary = lib
            isAddedToLearn = lrn
        }
    }

    fun changeSavedState() = performIOOperation(
        _libraryAddStatus,
        if (!isAddedToLibrary) addedLibrarySnackbarMessages else removedLibrarySnackbarMessages
    ) {
        repository.changePlaylistSavedState(playlistId)
        isAddedToLibrary = !isAddedToLibrary
    }


    fun changeLearnState() = performIOOperation(
        _learnAddStatus,
        if (!isAddedToLearn) addedLearnSnackbarMessages else removedLearnSnackbarMessages
    ) {
        repository.changePlaylistLearnState(playlistId)
        isAddedToLearn = !isAddedToLearn
    }


    @AssistedInject.Factory
    interface Factory {
        fun create(playlistId: String): PlaylistDetailsViewModel
    }
}