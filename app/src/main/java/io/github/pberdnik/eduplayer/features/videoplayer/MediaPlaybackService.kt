package io.github.pberdnik.eduplayer.features.videoplayer

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
import android.support.v4.media.session.MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.ACTION_PAUSE
import android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY
import android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_PAUSE
import android.support.v4.media.session.PlaybackStateCompat.ACTION_SEEK_TO
import android.support.v4.media.session.PlaybackStateCompat.ACTION_STOP
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import io.github.pberdnik.eduplayer.R
import io.github.pberdnik.eduplayer.domain.DeviceVideo

class MediaPlaybackService : Service() {
    companion object {
        const val EXTRA_DEVICE_VIDEO = "io.github.pberdnik.eduplayer.EXTRA_DEVICE_VIDEO"
        val LOG_TAG = MediaPlaybackService::class.java.simpleName
        const val PLAYBACK_CHANNEL_ID = "playback_channel"
        const val PLAYBACK_NOTIFICATION_ID = 1
    }

    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSourceFactory: ProgressiveMediaSource.Factory
    private lateinit var playerNotificationManager: PlayerNotificationManager
    private val playbackStateBuilder = PlaybackStateCompat.Builder()
    private lateinit var deviceVideo: DeviceVideo
    private var bitmap: Bitmap? = null


    override fun onCreate() {
        super.onCreate()
        player = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector())
        mediaSourceFactory = ProgressiveMediaSource.Factory(
            DefaultDataSourceFactory(
                this,
                Util.getUserAgent(this, getString(R.string.app_name))
            )
        )
        mediaSession = MediaSessionCompat(this, LOG_TAG).apply {
            setFlags(FLAG_HANDLES_MEDIA_BUTTONS or FLAG_HANDLES_TRANSPORT_CONTROLS)
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onSeekTo(pos: Long) = player.seekTo(pos)

                override fun onPlay() {
                    player.playWhenReady = true
                }

                override fun onPause() {
                    player.playWhenReady = false
                }
            })
        }

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            this,
            PLAYBACK_CHANNEL_ID,
            R.string.playback_channel_name,
            R.string.playback_channel_description,
            PLAYBACK_NOTIFICATION_ID,
            object : PlayerNotificationManager.MediaDescriptionAdapter {
                override fun getCurrentContentTitle(player: Player): String {
                    return deviceVideo.title
                }

                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    return PendingIntent.getActivity(
                        this@MediaPlaybackService,
                        0,
                        Intent(this@MediaPlaybackService, VideoPlayerActivity::class.java),
                        0
                    )
                }

                override fun getCurrentContentText(player: Player): String? {
                    return deviceVideo.duration
                }

                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): Bitmap? {
                    Glide.with(this@MediaPlaybackService).asBitmap().load(deviceVideo.uri).into(
                        object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                bitmap = resource
                                callback.onBitmap(resource)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        }
                    )
                    return null
                }
            },
            object :
                PlayerNotificationManager.NotificationListener {
                override fun onNotificationStarted(
                    notificationId: Int,
                    notification: Notification?
                ) {
                    startForeground(notificationId, notification)
                }

                override fun onNotificationCancelled(notificationId: Int) {
                    stopSelf()
                }
            }
        )
        with(playerNotificationManager) {
            setUseNavigationActions(false)
            setUseStopAction(true)
            setColorized(false)
            setUseChronometer(true)
            setSmallIcon(R.drawable.ic_edu_logo_notification)
        }
        playerNotificationManager.setPlayer(player)

        mediaSession.isActive = true
        playerNotificationManager.setMediaSessionToken(mediaSession.sessionToken)

        val mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setQueueNavigator(object : TimelineQueueNavigator(mediaSession) {
            override fun getMediaDescription(
                player: Player,
                windowIndex: Int
            ): MediaDescriptionCompat {
                return getMediaDescription()
            }
        })
        mediaSessionConnector.setPlayer(player)
    }

    internal fun getMediaDescription(): MediaDescriptionCompat {
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) throw RuntimeException("Can't start service due to null intent")
        deviceVideo = intent.getParcelableExtra(EXTRA_DEVICE_VIDEO)
        val mediaSource = mediaSourceFactory.createMediaSource(Uri.parse(deviceVideo.uri))
        bitmap = getDrawable(R.drawable.ic_school_black_24dp)?.toBitmap()
        player.prepare(mediaSource)
        player.playWhenReady = true
        return START_STICKY
    }

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        val player
            get() = this@MediaPlaybackService.player
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
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
        player.release()
        super.onDestroy()
    }
}
