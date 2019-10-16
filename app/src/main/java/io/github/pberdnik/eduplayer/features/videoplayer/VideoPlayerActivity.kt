package io.github.pberdnik.eduplayer.features.videoplayer

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.navArgs
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
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

    var player: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<VideoPlayerActivityBinding>(
            this, R.layout.video_player_activity
        ).also {
            it.vm = viewModel
            it.lifecycleOwner = this
        }
    }

    override fun onStart() {
        super.onStart()
        player = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector())
        binding.playerView.player = player
        val dataSourceFactory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, getString(R.string.app_name))
        )
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(args.uri))
        player?.prepare(mediaSource)
        player?.playWhenReady = true
    }

    override fun onStop() {
        binding.playerView.player = null
        player?.release()
        player = null
        super.onStop()
    }
}