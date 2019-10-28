package io.github.pberdnik.eduplayer.features.videoplayer

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.ACTION_PAUSE
import android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY
import android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_PAUSE
import android.support.v4.media.session.PlaybackStateCompat.ACTION_SEEK_TO
import android.support.v4.media.session.PlaybackStateCompat.ACTION_STOP
import androidx.core.graphics.drawable.toBitmap
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import io.github.pberdnik.eduplayer.R
import io.github.pberdnik.eduplayer.domain.DeviceVideo

class MediaPlaybackService : Service() {

    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var playerNotificationManager: PlayerNotificationManager
    private val playbackStateBuilder = PlaybackStateCompat.Builder()
    private var bitmap: Bitmap? = null


    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        val service get() = this@MediaPlaybackService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    fun init(deviceVideo: DeviceVideo, player: SimpleExoPlayer) {
        mediaSession = createMediaSession(player).apply {
            isActive = true
        }

        playerNotificationManager = createNotification(this, deviceVideo).apply {
            setPlayer(player)
            setMediaSessionToken(mediaSession.sessionToken)
        }

        val mediaSessionConnector =
            createMediaSessionConnector(mediaSession, deviceVideo, bitmap!!).apply {
                setPlayer(player)
            }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) throw RuntimeException("Can't start service due to null intent")
        bitmap = getDrawable(R.drawable.ic_school_black_24dp)?.toBitmap()
        return START_STICKY
    }

    private fun updatePlaybackState() {
        playbackStateBuilder
            // Available actions
            .setActions(
                ACTION_PLAY_PAUSE or ACTION_PLAY or ACTION_PAUSE or ACTION_STOP or ACTION_SEEK_TO
            )
            // Current playback state
            .setState(
                if (player.isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_STOPPED,
                0, // Track position in ms
                1.0f
            ) // Playback speed
        mediaSession.setPlaybackState(playbackStateBuilder.build())
    }

    override fun onDestroy() {
        player.playWhenReady = false
        player.release()
        super.onDestroy()
    }
}
