package io.github.pberdnik.eduplayer.features.learn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import io.github.pberdnik.eduplayer.R
import io.github.pberdnik.eduplayer.databinding.LearnFragmentBinding
import io.github.pberdnik.eduplayer.di.injector
import io.github.pberdnik.eduplayer.di.viewModel
import io.github.pberdnik.eduplayer.features.explore.playlistrecyclerview.PlaylistAdapter
import io.github.pberdnik.eduplayer.features.explore.playlistrecyclerview.PlaylistClickListener

class LearnFragment : Fragment() {

    private val viewModel by viewModel {
        injector.learnViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = LearnFragmentBinding.inflate(inflater).also {
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
                    LearnFragmentDirections.actionShowDetails(playlistId)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // should be:
        // item.onNavDestinationSelected(view!!.findNavController()) || super.onOptionsItemSelected(item)
        // but there is no way to provide custom animation. So here is the copy of
        // onNavDestinationSelected() with custom animations
        val navController = view!!.findNavController()
        val builder = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.slide_in_top)
            .setExitAnim(R.anim.no_animation)
            .setPopEnterAnim(R.anim.no_animation)
            .setPopExitAnim(R.anim.slide_out_bottom)
        if (item.order and Menu.CATEGORY_SECONDARY == 0) {
            builder.setPopUpTo(findStartDestination(navController.graph).id, false)
        }
        val options = builder.build()
        try {
            navController.navigate(item.itemId, null, options)
            return true
        } catch (e: IllegalArgumentException) {
            return super.onOptionsItemSelected(item)
        }
    }

    private fun findStartDestination(graph: NavGraph): NavDestination {
        var startDestination: NavDestination = graph
        while (startDestination is NavGraph) {
            val parent = startDestination as NavGraph?
            startDestination = parent!!.findNode(parent.startDestination)!!
        }
        return startDestination
    }
}
