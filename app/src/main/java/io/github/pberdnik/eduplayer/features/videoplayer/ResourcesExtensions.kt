package io.github.pberdnik.eduplayer.features.videoplayer

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.content.res.Resources
import androidx.annotation.Px
import io.github.pberdnik.eduplayer.R

// Helper functions to deal with fullscreen mode

public val Resources.statusBarHeight: Int @Px get() {
    val id = getIdentifier("status_bar_height", "dimen", "android")
    return when {
        id > 0 -> getDimensionPixelSize(id)
        else -> 0
    }
}

val Resources.navBarHeight: Int
    @Px get() {
        val id = getIdentifier("navigation_bar_height", "dimen", "android")
        return when {
            id > 0 -> getDimensionPixelSize(id)
            else -> 0
        }
    }

val Resources.showsSoftwareNavBar: Boolean
    get() {
        val id = getIdentifier("config_showNavigationBar", "bool", "android")
        return id > 0 && getBoolean(id)
    }

inline val Resources.isTablet: Boolean get() = getBoolean(R.bool.is_tablet)

inline val Resources.isNavBarAtBottom: Boolean get() {
    // Navbar is always on the bottom of the screen in portrait mode, but may
    // rotate with device if its category is sw600dp or above.
    return this.isTablet || this.configuration.orientation == ORIENTATION_PORTRAIT
}