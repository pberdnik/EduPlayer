package io.github.pberdnik.eduplayer.feat.explore

import android.app.Application
import androidx.lifecycle.*
import io.github.pberdnik.eduplayer.database.getDatabase
import io.github.pberdnik.eduplayer.domain.PlaylistData
import io.github.pberdnik.eduplayer.domain.PlaylistWithInfo
import io.github.pberdnik.eduplayer.repository.YoutubeRepository
import kotlinx.coroutines.launch

class ExploreViewModel(app: Application) : AndroidViewModel(app) {

    private val database = getDatabase(app)
    private val repository = YoutubeRepository(database)

    val _playlistsData = MediatorLiveData<List<PlaylistData>>()
    val playlistsData: LiveData<List<PlaylistData>> = _playlistsData

    init {
        _playlistsData.apply {
            addSource(repository.playlists) { combinePlaylistData() }
            addSource(repository.playlistExpansions) { combinePlaylistData() }
        }
        viewModelScope.launch {
            repository.refreshPlaylists()
        }
    }

    private fun combinePlaylistData() {
        val list = mutableListOf<PlaylistData>()
        val playlistExpansions = repository.playlistExpansions.value ?: listOf()
        repository.playlists.value?.forEach {pwi ->
            list.add(pwi)
            playlistExpansions.firstOrNull { it.id == pwi.playlistId }?.let {pe ->
                list.add(pe)
            }
        }
        _playlistsData.value = list
    }


    fun switchPlaylistExpansion(playlistWithInfo: PlaylistWithInfo) {
        viewModelScope.launch {
            repository.switchPlaylistExpansion(playlistWithInfo.playlist.id)
        }
    }


    /**
     * Factory for constructing ExploreViewModel with parameter
     */
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
