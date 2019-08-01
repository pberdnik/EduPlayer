package io.github.pberdnik.eduplayer.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.github.pberdnik.eduplayer.databinding.DemoFragmentBinding

class DemoFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(DemoViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DemoFragmentBinding.inflate(inflater)
        return binding.root
    }

}
