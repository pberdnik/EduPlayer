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

class VideoPlayerActivity : AppCompatActivity() {

    val viewModel by viewModel {
        injector.videoPlayerViewModel
    }

    private val args by navArgs<VideoPlayerActivityArgs>()

    lateinit var binding: VideoPlayerActivityBinding

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MediaPlaybackService.LocalBinder
            binding.playerView.player = binder.player
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            binding.playerView.player = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<VideoPlayerActivityBinding>(
            this, R.layout.video_player_activity
        ).also {
            it.vm = viewModel
            it.lifecycleOwner = this
        }

        val intent = Intent(this, MediaPlaybackService::class.java).apply {
            putExtra(MediaPlaybackService.EXTRA_URI, args.uri)
        }
        Util.startForegroundService(this, intent)
    }

    override fun onStart() {
        super.onStart()
        bindService(
            Intent(this, MediaPlaybackService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }
}