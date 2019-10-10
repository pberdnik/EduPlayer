package io.github.pberdnik.eduplayer.repository

import dagger.Reusable
import io.github.pberdnik.eduplayer.database.LocalDao
import io.github.pberdnik.eduplayer.database.localentities.DatabaseDeviceVideo
import javax.inject.Inject

@Reusable
class LocalRepository @Inject constructor(
    val localDao: LocalDao
) {
    val deviceVideos = localDao.getVideos()

    suspend fun insertVideos(uris: List<String>) {
        val dbUris = uris.map { uri -> DatabaseDeviceVideo(uri = uri) }
        localDao.insertVideos(*dbUris.toTypedArray())
    }
}