package io.github.pberdnik.eduplayer.features.videoplayer

import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.SimpleExoPlayer

private val LOG_TAG = MediaPlaybackService::class.java.simpleName

fun createMediaSession(context: Context, player: SimpleExoPlayer) =
    MediaSessionCompat(context, LOG_TAG).apply {
        setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
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
