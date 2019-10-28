package io.github.pberdnik.eduplayer.features.videoplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.navArgs
import com.google.android.exoplayer2.util.Util
import io.github.pberdnik.eduplayer.R
import io.github.pberdnik.eduplayer.databinding.VideoPlayerActivityBinding
import io.github.pberdnik.eduplayer.di.injector
import io.github.pberdnik.eduplayer.di.viewModel
import timber.log.Timber

class VideoPlayerActivity : AppCompatActivity() {

    private val args by navArgs<VideoPlayerActivityArgs>()

    val viewModel by viewModel {
        injector.videoPlayerViewModelFactory.create(args.deviceVideo)
    }

    lateinit var binding: VideoPlayerActivityBinding

    lateinit var mediaPlaybackService: MediaPlaybackService

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MediaPlaybackService.LocalBinder
            mediaPlaybackService = binder.service.apply {
                init(viewModel.deviceVideo, viewModel.player, viewModel.mediaSession)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<VideoPlayerActivityBinding>(
            this, R.layout.video_player_activity
        ).also {
            it.vm = viewModel
            it.lifecycleOwner = this
            it.playerView.player = viewModel.player
        }

        bindService(
            Intent(this, MediaPlaybackService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onStart() {
        super.onStart()
        if (::mediaPlaybackService.isInitialized)
            mediaPlaybackService.stopForeground(true)
    }

    override fun onStop() {
        if (!isChangingConfigurations && !isFinishing) {
            Timber.d("STARTING NOTIFICATION")
            Util.startForegroundService(this, Intent(this, MediaPlaybackService::class.java))
        }
        super.onStop()
    }

    override fun onDestroy() {
        unbindService(connection)
        super.onDestroy()
    }
}