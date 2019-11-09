package io.github.pberdnik.eduplayer.features.videoplayer

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.navigation.navArgs
import io.github.pberdnik.eduplayer.R
import io.github.pberdnik.eduplayer.customview.GesturePlayerControlView
import io.github.pberdnik.eduplayer.customview.playbackspeedcontrol.PlaybackSpeedControl
import io.github.pberdnik.eduplayer.databinding.VideoPlayerActivityBinding
import io.github.pberdnik.eduplayer.di.injector
import io.github.pberdnik.eduplayer.di.viewModel

class VideoPlayerActivity : AppCompatActivity() {

    private val args by navArgs<VideoPlayerActivityArgs>()

    private val viewModel by viewModel {
        injector.videoPlayerViewModelFactory.create(args.deviceVideo)
    }

    private lateinit var binding: VideoPlayerActivityBinding

    private lateinit var videoContainer: FrameLayout

    private val onControlsListener = object : GesturePlayerControlView.OnChangeListener {
        override fun onLocationChanged(
            playerControlsView: GesturePlayerControlView,
            scaleFactor: Float,
            translateX: Float,
            translateY: Float
        ) {
            videoContainer.run {
                translationX = translateX
                translationY = translateY
                scaleX = scaleFactor
                scaleY = scaleFactor
            }
        }

        override fun onRewindFactorChanged(
            playerControlsView: GesturePlayerControlView,
            rewindFactor: Float,
            isLastChange: Boolean
        ) {
            viewModel.rewind(rewindFactor, isLastChange)
        }

        override fun onSingleTap(playerControlsView: GesturePlayerControlView) {
            viewModel.playOrPause()
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
            it.gesturePlayerControlView.onChangeListener = onControlsListener
            videoContainer = it.playerView.findViewById(R.id.exo_content_frame)
            it.playbackSpeedControl.onChangeListener = object : PlaybackSpeedControl.OnChangeListener {
                override fun onSpeedValueChanged(playbackSpeedControl: PlaybackSpeedControl, speedValue: Float) {

                }
            }
        }

        viewModel.turnOnFullscreen()

        viewModel.isFullscreen.observe(this) { isFullscreen ->
            if (isFullscreen) hideSystemUI()
            else showSystemUI()
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

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) viewModel.turnOnFullscreen()
        else viewModel.turnOffFullscreen()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}
