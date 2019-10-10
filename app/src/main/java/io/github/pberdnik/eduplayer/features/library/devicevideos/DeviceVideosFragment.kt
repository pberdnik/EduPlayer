package io.github.pberdnik.eduplayer.features.library.devicevideos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.pberdnik.eduplayer.databinding.DeviceVideosFragmentBinding
import io.github.pberdnik.eduplayer.di.injector
import io.github.pberdnik.eduplayer.di.viewModel

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
            it.deviceVideosRv.adapter = DeviceVideoAdapter(DeviceVideoClickListener { })
        }

        return binding.root
    }

}
