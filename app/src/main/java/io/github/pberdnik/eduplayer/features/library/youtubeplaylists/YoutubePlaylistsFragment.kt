package io.github.pberdnik.eduplayer.features.library.youtubeplaylists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.pberdnik.eduplayer.databinding.YoutubePlaylistFragmentBinding
import io.github.pberdnik.eduplayer.di.injector
import io.github.pberdnik.eduplayer.di.viewModel

class YoutubePlaylistsFragment : Fragment() {

    private val viewModel by viewModel {
        injector.youtubePlaylistsViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = YoutubePlaylistFragmentBinding.inflate(inflater).also {
            it.vm = viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

}
