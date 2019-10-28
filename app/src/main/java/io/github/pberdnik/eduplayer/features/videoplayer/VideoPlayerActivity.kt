package io.github.pberdnik.eduplayer.features.videoplayer

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.navArgs
import io.github.pberdnik.eduplayer.R
import io.github.pberdnik.eduplayer.databinding.VideoPlayerActivityBinding
import io.github.pberdnik.eduplayer.di.injector
import io.github.pberdnik.eduplayer.di.viewModel

class VideoPlayerActivity : AppCompatActivity() {

    private val args by navArgs<VideoPlayerActivityArgs>()

    val viewModel by viewModel {
        injector.videoPlayerViewModelFactory.create(args.deviceVideo)
    }

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
            it.playerView.player = viewModel.player
        }
    }

    override fun onStart() {
        super.onStart()
//        bindService(
//            Intent(this, MediaPlaybackService::class.java),
//            connection,
//            Context.BIND_AUTO_CREATE
//        )
    }

    override fun onStop() {
        super.onStop()
//        unbindService(connection)
    }
}