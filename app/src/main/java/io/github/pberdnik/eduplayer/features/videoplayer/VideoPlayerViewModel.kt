package io.github.pberdnik.eduplayer.features.videoplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.Util
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.github.pberdnik.eduplayer.domain.DeviceVideo

class VideoPlayerViewModel @AssistedInject constructor(
    private val context: Context,
    mediaSourceFactory: ProgressiveMediaSource.Factory,
    @Assisted val deviceVideo: DeviceVideo
) : ViewModel() {

    val player: SimpleExoPlayer =
        ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
    val mediaSession: MediaSessionCompat

    private var mediaPlaybackService: MediaPlaybackService? = null

    init {
        val mediaSource = mediaSourceFactory.createMediaSource(Uri.parse(deviceVideo.uri))
        player.prepare(mediaSource)
        player.playWhenReady = true
        mediaSession = createMediaSession(context, player).apply {
            isActive = true
        }
    }

    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MediaPlaybackService.LocalBinder
            mediaPlaybackService = binder.service.apply {
                init(deviceVideo, player, mediaSession)
            }
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    init {
        context.bindService(
            Intent(context, MediaPlaybackService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    fun showPlayerNotification() {
        Util.startForegroundService(context, Intent(context, MediaPlaybackService::class.java))
    }

    fun hideNotification() {
        mediaPlaybackService?.run {
            clearNotification()
            stopForeground(true)
        }
    }

    override fun onCleared() {
        mediaPlaybackService?.stopForeground(true)
        mediaPlaybackService?.stopSelf()
        context.unbindService(connection)
        player.playWhenReady = false
        player.release()
        super.onCleared()
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(deviceVideo: DeviceVideo): VideoPlayerViewModel
    }
}