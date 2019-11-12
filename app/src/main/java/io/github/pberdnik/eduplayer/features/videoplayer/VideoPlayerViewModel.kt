package io.github.pberdnik.eduplayer.features.videoplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.Util
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.github.pberdnik.eduplayer.domain.DeviceVideo
import java.util.TreeMap

private const val REWIND_SCALE = 60_000

data class PlayerLocation(
    var translateX: Float = 0f,
    var translateY: Float = 0f,
    var scaleFactor: Float = 1f
)

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

    private val playbackParametersHolder = TreeMap<Float, PlaybackParameters>()

    init {
        // speeds from 0.25 to 4
        for (speedX16 in 4..64) {
            val speed = speedX16 / 16f
            playbackParametersHolder[speed] = PlaybackParameters(speed)
        }
    }

    private val _isFullscreen = MutableLiveData<Boolean>(true)
    val isFullscreen: LiveData<Boolean> = _isFullscreen

    val playerLocation = PlayerLocation()

    fun turnOnFullscreen() {
        _isFullscreen.value = true
    }

    fun turnOffFullscreen() {
        _isFullscreen.value = false
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

    fun playOrPause() {
        player.playWhenReady = !player.playWhenReady
    }

    override fun onCleared() {
        mediaPlaybackService?.stopForeground(true)
        mediaPlaybackService?.stopSelf()
        context.unbindService(connection)
        player.playWhenReady = false
        player.release()
        super.onCleared()
    }

    private var isRewinding = false
    private var posBeforeRewind = 0L

    fun rewind(rewindFactor: Float, lastChange: Boolean) {
        if (lastChange) {
            player.playWhenReady = true
            isRewinding = false
            return
        }
        if (!isRewinding) {
            isRewinding = true
            posBeforeRewind = player.currentPosition
        }
        val seekTo = (posBeforeRewind + REWIND_SCALE * rewindFactor).toLong()
        player.seekTo(seekTo.coerceIn(0, player.duration))
        player.playWhenReady = false
    }

    fun changeLocation(translateX: Float, translateY: Float, scaleFactor: Float) {
        playerLocation.let {
            it.translateX = translateX
            it.translateY = translateY
            it.scaleFactor = scaleFactor
        }
    }

    fun changePlaybackSpeed(speedValue: Float) {
        player.playbackParameters = playbackParametersHolder.ceilingEntry(speedValue)?.value
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(deviceVideo: DeviceVideo): VideoPlayerViewModel
    }
}