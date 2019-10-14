package io.github.pberdnik.eduplayer.repository

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.OpenableColumns
import dagger.Reusable
import io.github.pberdnik.eduplayer.database.LocalDao
import io.github.pberdnik.eduplayer.database.localentities.DatabaseDeviceVideo
import javax.inject.Inject

@Reusable
class LocalRepository @Inject constructor(
    private val localDao: LocalDao,
    private val contentResolver: ContentResolver,
    private val context: Context
) {
    val deviceVideos = localDao.getVideos()

    suspend fun insertVideos(uris: List<Uri>, intentFlags: Int) {
        val dbUris = uris.map { uri -> getDeviceVideoWithMetaData(uri, intentFlags) }
        localDao.insertVideos(*dbUris.toTypedArray())
    }

    private fun getDeviceVideoWithMetaData(uri: Uri, intentFlags: Int): DatabaseDeviceVideo {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri)
        // persist the permissions for uri access
        val takeFlags = intentFlags and (Intent.FLAG_GRANT_READ_URI_PERMISSION
                or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        contentResolver.takePersistableUriPermission(uri, takeFlags)
        val duration = convertToHumanDuration(
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()
        )
        retriever.release()

        var displayName: String? = null
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }

        return DatabaseDeviceVideo(uri = uri, title = displayName, duration = duration)
    }

    private fun convertToHumanDuration(durationMillis: Long): String {
        val durationSeconds = durationMillis / 1000
        val h = durationSeconds / 60 / 60
        val m = durationSeconds % (60 * 60) / 60
        val s = durationSeconds % 60
        return if (h == 0L) {
            String.format("%d:%02d", m, s)
        } else {
            String.format("%d:%02d:%02d", h, m, s)
        }
    }
}