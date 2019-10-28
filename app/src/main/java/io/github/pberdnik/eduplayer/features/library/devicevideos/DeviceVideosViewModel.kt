package io.github.pberdnik.eduplayer.features.library.devicevideos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.pberdnik.eduplayer.domain.DeviceVideo
import io.github.pberdnik.eduplayer.repository.LocalRepository
import io.github.pberdnik.eduplayer.util.Event
import javax.inject.Inject

class DeviceVideosViewModel @Inject constructor(
    localRepository: LocalRepository
): ViewModel() {
    val deviceVideos = localRepository.deviceVideos

    private val _navigateToVideoPlayer = MutableLiveData<Event<DeviceVideo>>()
    val navigateToVideoPlayer: LiveData<Event<DeviceVideo>> = _navigateToVideoPlayer

    fun openVideoPlayer(deviceVideo: DeviceVideo) {
        _navigateToVideoPlayer.value = Event(deviceVideo)
    }
}
