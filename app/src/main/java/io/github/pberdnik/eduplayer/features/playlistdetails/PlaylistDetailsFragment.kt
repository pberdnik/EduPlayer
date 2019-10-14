package io.github.pberdnik.eduplayer.features.playlistdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import io.github.pberdnik.eduplayer.databinding.PlaylistDetailsFragmentBinding
import io.github.pberdnik.eduplayer.di.injector
import io.github.pberdnik.eduplayer.di.viewModel
import io.github.pberdnik.eduplayer.util.observeSnackbars

class PlaylistDetailsFragment : Fragment() {

    private val args: PlaylistDetailsFragmentArgs by navArgs()

    lateinit var binding: PlaylistDetailsFragmentBinding

    private val viewModel by viewModel {
        injector.playlistDetailsViewModelFactory.create(args.playlistId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PlaylistDetailsFragmentBinding.inflate(inflater).also {
            it.vm = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.playlistItemsRv.adapter = PlaylistItemAdapter(PlaylistItemClickListener {})
        }

        observeSnackbars(
            binding.rootCl,
            viewModel.refreshStatus,
            viewModel.libraryAddStatus,
            viewModel.learnAddStatus
        )

        binding.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.title = "Playlist"
        }
    }

}