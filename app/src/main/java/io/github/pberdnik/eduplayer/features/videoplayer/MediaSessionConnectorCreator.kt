package io.github.pberdnik.eduplayer.features.videoplayer

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import io.github.pberdnik.eduplayer.domain.DeviceVideo

fun createMediaSessionConnector(
    mediaSession: MediaSessionCompat,
    deviceVideo: DeviceVideo,
    bitmap: Bitmap
) =
    MediaSessionConnector(mediaSession).apply {
        setQueueNavigator(object : TimelineQueueNavigator(mediaSession) {
            override fun getMediaDescription(
                player: Player,
                windowIndex: Int
            ): MediaDescriptionCompat {
                return getMediaDescription(deviceVideo, bitmap)
            }
        })
    }


private fun getMediaDescription(deviceVideo: DeviceVideo, bitmap: Bitmap): MediaDescriptionCompat {
    val extras = Bundle()
    extras.putParcelable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap)
    extras.putParcelable(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, bitmap)
    return MediaDescriptionCompat.Builder()
        .setMediaId(deviceVideo.title)
        .setIconBitmap(bitmap)
        .setTitle(deviceVideo.title)
        .setDescription(deviceVideo.duration)
        .setExtras(extras)
        .build()
}