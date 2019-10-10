package io.github.pberdnik.eduplayer.repository

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import dagger.Reusable
import io.github.pberdnik.eduplayer.database.LocalDao
import io.github.pberdnik.eduplayer.database.localentities.DatabaseDeviceVideo
import javax.inject.Inject

@Reusable
class LocalRepository @Inject constructor(
    val localDao: LocalDao,
    val contentResolver: ContentResolver
) {
    val deviceVideos = localDao.getVideos()

    suspend fun insertVideos(uris: List<String>) {
        val dbUris = uris.map { uri ->
            val (title, duration) = dumpVideoMetaData(Uri.parse(uri))
            DatabaseDeviceVideo(uri = uri, title = title, duration = duration)
        }
        localDao.insertVideos(*dbUris.toTypedArray())
    }

    fun dumpVideoMetaData(uri: Uri): Pair<String?, String?> {
        var displayName: String? = null
        var size: String? = null
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                val sizeIndex: Int = it.getColumnIndex(OpenableColumns.SIZE)
                size = if (!it.isNull(sizeIndex)) it.getString(sizeIndex) else "Unknown"
            }
        }
        return displayName to size
    }
}