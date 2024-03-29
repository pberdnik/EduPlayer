package io.github.pberdnik.eduplayer.features.library.youtubeplaylists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import io.github.pberdnik.eduplayer.databinding.YoutubePlaylistFragmentBinding
import io.github.pberdnik.eduplayer.di.injector
import io.github.pberdnik.eduplayer.di.viewModel
import io.github.pberdnik.eduplayer.features.explore.playlistrecyclerview.PlaylistAdapter
import io.github.pberdnik.eduplayer.features.explore.playlistrecyclerview.PlaylistClickListener
import io.github.pberdnik.eduplayer.features.library.LibraryFragmentDirections
import io.github.pberdnik.eduplayer.util.observeSnackbar

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
            it.playlistRvLayout.playlistRv.adapter =
                PlaylistAdapter(PlaylistClickListener { playlistWithInfo ->
                    viewModel.displayPlaylistDetails(playlistWithInfo.playlist.id)
                })
        }

        viewModel.navigateToSelectedPlaylistItem.observe(this, Observer { event ->
            event.getIfNotHandled()?.let { playlistId ->
                findNavController().navigate(
                    LibraryFragmentDirections.actionShowDetails(playlistId)
                )
            }
        })

        observeSnackbar(binding.rootCl, viewModel.refreshStatus)

        return binding.root
    }

}
