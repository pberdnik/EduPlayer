package io.github.pberdnik.eduplayer.features.videoplayer

import android.content.Context
import android.net.Uri
import android.support.v4.media.session.MediaSessionCompat
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.github.pberdnik.eduplayer.domain.DeviceVideo

class VideoPlayerViewModel @AssistedInject constructor(
    context: Context,
    mediaSourceFactory: ProgressiveMediaSource.Factory,
    @Assisted val deviceVideo: DeviceVideo
) : ViewModel() {

    val player: SimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
    val mediaSession: MediaSessionCompat

    init {
        val mediaSource = mediaSourceFactory.createMediaSource(Uri.parse(deviceVideo.uri))
        player.prepare(mediaSource)
        player.playWhenReady = true
        mediaSession = createMediaSession(context, player).apply {
            isActive = true
        }
    }

    override fun onCleared() {
        player.playWhenReady = false
        player.release()
        super.onCleared()
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(deviceVideo: DeviceVideo): VideoPlayerViewModel
    }
}