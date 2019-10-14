package io.github.pberdnik.eduplayer.features.library

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import io.github.pberdnik.eduplayer.databinding.LibraryFragmentBinding
import io.github.pberdnik.eduplayer.di.injector
import io.github.pberdnik.eduplayer.di.viewModel
import io.github.pberdnik.eduplayer.features.NavigationRootFragment


const val SELECT_VIDEOS = 1000

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
            it.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    when (position) {
                        1 -> binding.addFab.show()
                        else -> binding.addFab.hide()
                    }
                }
            })
        }

        binding.addFab.setOnClickListener {
            chooseVideos()
        }

        return binding.root
    }

    private fun chooseVideos() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "video/*"
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(
            Intent.createChooser(intent, "Select Videos"),
            SELECT_VIDEOS
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_VIDEOS && resultCode == Activity.RESULT_OK) {
            if (data!!.clipData != null) {
                val count = data.clipData!!.itemCount
                var currentItem = 0
                var str = ""
                val uris = mutableListOf<Uri>()
                while (currentItem < count) {
                    val videoUri: Uri = data.clipData!!.getItemAt(currentItem).uri
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                    currentItem += 1
                    str += videoUri.toString() + "\n"
                    uris += videoUri
                }
                viewModel.insertURIs(uris, data.flags)
                Toast.makeText(context, str, Toast.LENGTH_LONG).show()
            } else if (data.data != null) {
                val uris = mutableListOf<Uri>(data.data!!)
                viewModel.insertURIs(uris, data.flags)
            }
        }
    }
}
