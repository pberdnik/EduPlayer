package io.github.pberdnik.eduplayer.feat.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.github.pberdnik.eduplayer.databinding.ExploreFragmentBinding

class ExploreFragment : Fragment() {

    private val viewModel by viewModels<ExploreViewModel> {
        val app = requireNotNull(this.activity).application
        ExploreViewModel.Factory(app)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = ExploreFragmentBinding.inflate(inflater).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.playlistRv.adapter = PlaylistAdapter()
        }
        return binding.root
    }

}
