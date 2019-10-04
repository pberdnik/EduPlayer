package io.github.pberdnik.eduplayer.features.library.eduplayerplaylists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.pberdnik.eduplayer.databinding.EduplayerPlaylistFragmentBinding
import io.github.pberdnik.eduplayer.di.injector
import io.github.pberdnik.eduplayer.di.viewModel

class EduPlayerPlaylistsFragment : Fragment() {

    private val viewModel by viewModel {
        injector.eduPlayerPlaylistsViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = EduplayerPlaylistFragmentBinding.inflate(inflater).also {
            it.vm = viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

}
