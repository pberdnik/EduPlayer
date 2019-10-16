package io.github.pberdnik.eduplayer.features.library.devicevideos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import io.github.pberdnik.eduplayer.databinding.DeviceVideosFragmentBinding
import io.github.pberdnik.eduplayer.di.injector
import io.github.pberdnik.eduplayer.di.viewModel
import io.github.pberdnik.eduplayer.features.library.LibraryFragmentDirections

class DeviceVideosFragment : Fragment() {

    private val viewModel by viewModel {
        injector.deviceVideosViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DeviceVideosFragmentBinding.inflate(inflater).also {
            it.vm = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.deviceVideosRv.adapter = DeviceVideoAdapter(DeviceVideoClickListener {dv ->
                viewModel.openVideoPlayer(dv.uri)
            })
        }

        viewModel.navigateToVideoPlayer.observe(viewLifecycleOwner) {event ->
            event.getIfNotHandled()?.let { uri ->
                findNavController().navigate(LibraryFragmentDirections.actionShowVideoPlayer(uri))
            }
        }

        return binding.root
    }

}
