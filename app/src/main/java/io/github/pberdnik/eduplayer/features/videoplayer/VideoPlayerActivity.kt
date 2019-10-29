package io.github.pberdnik.eduplayer.features.videoplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.navArgs
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
        viewModel.hideNotification()
    }

    override fun onStop() {
        if (!isChangingConfigurations && !isFinishing) {
            viewModel.showPlayerNotification()
        }
        super.onStop()
    }
}