package io.github.pberdnik.eduplayer.features.videoplayer

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import io.github.pberdnik.eduplayer.R
import io.github.pberdnik.eduplayer.domain.DeviceVideo
import timber.log.Timber

class MediaPlaybackService : Service() {

    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var playerNotificationManager: PlayerNotificationManager
    private var bitmap: Bitmap? = null
    private lateinit var deviceVideo: DeviceVideo


    inner class LocalBinder : Binder() {
        val service get() = this@MediaPlaybackService
    }

    private val binder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder? = binder

    private val notificationInfo = NotificationInfo()

    fun init(deviceVideo: DeviceVideo, player: SimpleExoPlayer, mediaSession: MediaSessionCompat) {
        this.deviceVideo = deviceVideo
        this.player = player
        this.mediaSession = mediaSession

        bitmap = getDrawable(R.drawable.ic_school_black_24dp)?.toBitmap()

        playerNotificationManager = createNotification(this, deviceVideo, notificationInfo).apply {
            setMediaSessionToken(mediaSession.sessionToken)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("CALLED ON_START_COMMAND")
        playerNotificationManager.setPlayer(player)
        startForeground(notificationInfo.notificationId, notificationInfo.notification)
        return START_STICKY
    }

    fun clearNotification() {
        playerNotificationManager.setPlayer(null)
    }
}
