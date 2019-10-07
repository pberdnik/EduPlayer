package io.github.pberdnik.eduplayer.features

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import io.github.pberdnik.eduplayer.R

/**
 * Contains code common for all bottom navigation root fragments:
 * - Setting action bar
 * - Creating menu
 * - Defining animation for navigation from menu
 */
abstract class NavigationRootFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    protected abstract fun getToolbar(): Toolbar

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(getToolbar())
            supportActionBar?.setIcon(R.drawable.ic_edu_logo_with_padding)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    // should be:
    // item.onNavDestinationSelected(view!!.findNavController()) || super.onOptionsItemSelected(item)
    // but there is no way to provide custom animation. So here is the copy of
    // onNavDestinationSelected() with custom animations
    override fun onOptionsItemSelected(item: MenuItem): Boolean = try {
        view!!.findNavController().navigate(item.itemId, null, getOptionsWithAnimations(item))
        true
    } catch (e: IllegalArgumentException) {
        super.onOptionsItemSelected(item)
    }

    private fun getOptionsWithAnimations(item: MenuItem): NavOptions {
        val optionsBuilder = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.slide_in_top)
            .setExitAnim(R.anim.no_animation)
            .setPopEnterAnim(R.anim.no_animation)
            .setPopExitAnim(R.anim.slide_out_bottom)
        if (item.order and Menu.CATEGORY_SECONDARY == 0) {
            optionsBuilder.setPopUpTo(findStartDestination(view!!.findNavController().graph).id, false)
        }
        return optionsBuilder.build()
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
