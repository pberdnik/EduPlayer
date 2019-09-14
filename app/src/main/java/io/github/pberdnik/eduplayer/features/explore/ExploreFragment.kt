package io.github.pberdnik.eduplayer.features.explore

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import io.github.pberdnik.eduplayer.R
import io.github.pberdnik.eduplayer.databinding.ExploreFragmentBinding
import io.github.pberdnik.eduplayer.features.explore.playlistrecyclerview.PlaylistAdapter
import io.github.pberdnik.eduplayer.features.explore.playlistrecyclerview.PlaylistClickListener

class ExploreFragment : Fragment() {

    private val viewModel by viewModels<ExploreViewModel> {
        val app = requireNotNull(this.activity).application
        ExploreViewModel.Factory(app)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ExploreFragmentBinding.inflate(inflater).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.playlistRv.adapter =
                PlaylistAdapter(PlaylistClickListener { playlistWithInfo ->
                    viewModel.displayPlaylistDetails(playlistWithInfo.playlist.id)
                })
        }

        viewModel.navigateToSelectedPlaylistItem.observe(this, Observer { playlistId ->
            if (null != playlistId) {
                findNavController().navigate(
                    ExploreFragmentDirections.actionShowDetails(playlistId)
                )
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayPlaylistDetailsComplete()
            }
        })

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.appBar)
            supportActionBar?.setIcon(R.drawable.ic_edu_logo_with_padding)
        }

        setHasOptionsMenu(true)

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        item.onNavDestinationSelected(view!!.findNavController())
                || super.onOptionsItemSelected(item)
}
