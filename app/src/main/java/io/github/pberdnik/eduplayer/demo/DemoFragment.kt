package io.github.pberdnik.eduplayer.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.github.pberdnik.eduplayer.customview.playbackspeedcontrol.PlaybackSpeedControl
import io.github.pberdnik.eduplayer.databinding.DemoFragmentBinding

private const val eps = 0.005f

class DemoFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(DemoViewModel::class.java) }

    lateinit var binding: DemoFragmentBinding

    private val onSpeedValueChangeListener = object : PlaybackSpeedControl.OnChangeListener {
        override fun onSpeedValueChanged(playbackSpeedControl: PlaybackSpeedControl, speedValue: Float) {
            binding.speedValueTV.text = String.format("%.2f", speedValue)
            binding.speedValueTV.textSize = 12f * speedValue
            val changeSpeedValue: (PlaybackSpeedControl) -> Unit = {
                if (!(speedValue - eps < it.speedValue && it.speedValue < speedValue + eps)) {
                    it.speedValue = speedValue
                }
            }
            binding.playbackSpeedControl.let(changeSpeedValue)
            binding.playbackSpeedControl2.let(changeSpeedValue)
            binding.playbackSpeedControl3.let(changeSpeedValue)
            binding.playbackSpeedControl4.let(changeSpeedValue)
            binding.playbackSpeedControl5.let(changeSpeedValue)
            binding.playbackSpeedControl6.let(changeSpeedValue)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DemoFragmentBinding.inflate(inflater)
        binding.playbackSpeedControl.onChangeListener = onSpeedValueChangeListener
        binding.playbackSpeedControl2.onChangeListener = onSpeedValueChangeListener
        binding.playbackSpeedControl3.onChangeListener = onSpeedValueChangeListener
        binding.playbackSpeedControl4.onChangeListener = onSpeedValueChangeListener
        binding.playbackSpeedControl5.onChangeListener = onSpeedValueChangeListener
        binding.playbackSpeedControl6.onChangeListener = onSpeedValueChangeListener
        binding.playbackSpeedControl.speedValue = 1f
        return binding.root
    }

}
