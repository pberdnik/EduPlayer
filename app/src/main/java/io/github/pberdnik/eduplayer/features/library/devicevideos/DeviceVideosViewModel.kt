package io.github.pberdnik.eduplayer.features.library.devicevideos

import android.content.Context
import androidx.lifecycle.ViewModel
import io.github.pberdnik.eduplayer.repository.LocalRepository
import javax.inject.Inject

class DeviceVideosViewModel @Inject constructor(
    val localRepository: LocalRepository,
    val context: Context
): ViewModel() {
    val deviceVideos = localRepository.deviceVideos
}
