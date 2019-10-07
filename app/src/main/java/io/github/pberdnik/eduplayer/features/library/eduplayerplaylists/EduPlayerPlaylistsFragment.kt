package io.github.pberdnik.eduplayer.features.library.eduplayerplaylists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import io.github.pberdnik.eduplayer.databinding.EduplayerPlaylistFragmentBinding
import io.github.pberdnik.eduplayer.di.injector
import io.github.pberdnik.eduplayer.di.viewModel
import io.github.pberdnik.eduplayer.features.explore.playlistrecyclerview.PlaylistAdapter
import io.github.pberdnik.eduplayer.features.explore.playlistrecyclerview.PlaylistClickListener
import io.github.pberdnik.eduplayer.features.library.LibraryFragmentDirections

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
            it.playlistRv.adapter =
                PlaylistAdapter(PlaylistClickListener { playlistWithInfo ->
                    viewModel.displayPlaylistDetails(playlistWithInfo.playlist.id)
                })
        }

        viewModel.navigateToSelectedPlaylistItem.observe(this, Observer { playlistId ->
            if (null != playlistId) {
                findNavController().navigate(
                    LibraryFragmentDirections.actionShowDetails(playlistId)
                )
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayPlaylistDetailsComplete()
            }
        })

        return binding.root
    }

}
