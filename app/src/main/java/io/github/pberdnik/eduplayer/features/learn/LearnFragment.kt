package io.github.pberdnik.eduplayer.features.learn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import io.github.pberdnik.eduplayer.databinding.LearnFragmentBinding
import io.github.pberdnik.eduplayer.di.injector
import io.github.pberdnik.eduplayer.di.viewModel
import io.github.pberdnik.eduplayer.features.NavigationRootFragment
import io.github.pberdnik.eduplayer.features.explore.playlistrecyclerview.PlaylistAdapter
import io.github.pberdnik.eduplayer.features.explore.playlistrecyclerview.PlaylistClickListener

class LearnFragment : NavigationRootFragment() {

    lateinit var binding: LearnFragmentBinding

    private val viewModel by viewModel {
        injector.learnViewModel
    }

    override fun getToolbar(): Toolbar = binding.mainToolbar as Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LearnFragmentBinding.inflate(inflater).also {
            it.vm = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.playlistRvLayout.playlistRv.adapter =
                PlaylistAdapter(PlaylistClickListener { playlistWithInfo ->
                    viewModel.displayPlaylistDetails(playlistWithInfo.playlist.id)
                })
        }

        viewModel.navigateToSelectedPlaylistItem.observe(this, Observer { playlistId ->
            if (null != playlistId) {
                findNavController().navigate(
                    LearnFragmentDirections.actionShowDetails(playlistId)
                )
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayPlaylistDetailsComplete()
            }
        })

        return binding.root
    }
}
