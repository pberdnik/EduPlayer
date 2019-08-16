package io.github.pberdnik.eduplayer.feat.explore

import android.app.Application
import androidx.lifecycle.*
import io.github.pberdnik.eduplayer.database.getDatabase
import io.github.pberdnik.eduplayer.repository.YoutubeRepository
import kotlinx.coroutines.launch

class ExploreViewModel(app: Application) : AndroidViewModel(app) {

    private val database = getDatabase(app)
    private val repository = YoutubeRepository(database)

    private val _videos = Transformations.map(repository.playlists) {
        it.joinToString("\n")
    }
    val videos: LiveData<String> = _videos

    init {
        viewModelScope.launch {
            repository.refreshPlaylists()
        }
    }

    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExploreViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ExploreViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
