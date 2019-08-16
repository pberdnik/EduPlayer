package io.github.pberdnik.eduplayer.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.pberdnik.eduplayer.network.youtubeDataApiService
import kotlinx.coroutines.launch

class ExploreViewModel : ViewModel() {

    private val _videos = MutableLiveData<String>("Hell world")
    val videos: LiveData<String> = _videos

    init {
        viewModelScope.launch {
            _videos.value = youtubeDataApiService.getPlaylistsForChannel().toString()
        }
    }

}
