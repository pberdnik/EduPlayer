package io.github.pberdnik.eduplayer.features.library

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.github.pberdnik.eduplayer.features.library.devicevideos.DeviceVideosFragment
import io.github.pberdnik.eduplayer.features.library.eduplayerplaylists.EduPlayerPlaylistsFragment
import io.github.pberdnik.eduplayer.features.library.youtubeplaylists.YoutubePlaylistsFragment

class LibraryViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> EduPlayerPlaylistsFragment()
        1 -> DeviceVideosFragment()
        2 -> YoutubePlaylistsFragment()
        else -> throw IllegalArgumentException("Wrong fragment position: $position")
    }


}