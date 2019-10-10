package io.github.pberdnik.eduplayer.features.library

import android.net.Uri
import androidx.lifecycle.ViewModel
import io.github.pberdnik.eduplayer.repository.LocalRepository
import io.github.pberdnik.eduplayer.util.performIOOperation
import javax.inject.Inject

class LibraryViewModel @Inject constructor(
    val localRepository: LocalRepository
) : ViewModel() {
    fun insertURIs(uris: List<Uri>) {
        performIOOperation {
            localRepository.insertVideos(uris = uris.map { it.toString() })
        }
    }
}
