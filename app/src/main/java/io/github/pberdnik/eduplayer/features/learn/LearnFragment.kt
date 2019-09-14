package io.github.pberdnik.eduplayer.features.learn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.github.pberdnik.eduplayer.databinding.LearnFragmentBinding

class LearnFragment : Fragment() {

    val viewModel by viewModels<LearnViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = LearnFragmentBinding.inflate(inflater).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

}
