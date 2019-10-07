package io.github.pberdnik.eduplayer.features.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.google.android.material.tabs.TabLayoutMediator
import io.github.pberdnik.eduplayer.databinding.LibraryFragmentBinding
import io.github.pberdnik.eduplayer.di.injector
import io.github.pberdnik.eduplayer.di.viewModel
import io.github.pberdnik.eduplayer.features.NavigationRootFragment

class LibraryFragment : NavigationRootFragment() {

    lateinit var binding: LibraryFragmentBinding

    private val viewModel by viewModel {
        injector.libraryViewModel
    }

    override fun getToolbar(): Toolbar = binding.mainToolbar as Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LibraryFragmentBinding.inflate(inflater).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.viewPager.adapter =
                LibraryViewPagerAdapter(this)
            TabLayoutMediator(it.libraryTab, it.viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "EduPlayer"
                    1 -> "Device"
                    2 -> "YouTube"
                    else -> throw IllegalArgumentException("Wrong position: $position")
                }
            }.attach()
        }

        return binding.root
    }
}
