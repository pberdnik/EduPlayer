package io.github.pberdnik.eduplayer.feat.playlistdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import io.github.pberdnik.eduplayer.databinding.PlaylistDetailsFragmentBinding

class PlaylistDetailsFragment : Fragment() {

    private lateinit var viewModel: PlaylistDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val app = requireNotNull(this.activity).application
        val playlistId = PlaylistDetailsFragmentArgs.fromBundle(arguments!!).playlistId
        val factory = PlaylistDetailsViewModel.Factory(playlistId, app)
        viewModel = ViewModelProviders.of(this, factory)
            .get(PlaylistDetailsViewModel::class.java)

        val binding = PlaylistDetailsFragmentBinding.inflate(inflater).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.playlistItemsRv.adapter = PlaylistItemAdapter(PlaylistItemClickListener {})
        }

        return binding.root
    }

    private fun navigateUp(view: View) {
        view.findNavController().navigateUp()
    }
}