package io.github.pberdnik.eduplayer.feat.explore.playlistrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.pberdnik.eduplayer.R
import io.github.pberdnik.eduplayer.databinding.ListItemPlaylistBinding
import io.github.pberdnik.eduplayer.databinding.ListItemPlaylistExpansionBinding
import io.github.pberdnik.eduplayer.domain.PlaylistExpansion
import io.github.pberdnik.eduplayer.domain.PlaylistWithInfo

abstract class PlaylistViewHolder(val binding: ListItemPlaylistBinding) :
    RecyclerView.ViewHolder(binding.root) {

    open fun bind(playlistWithInfo: PlaylistWithInfo) {
        binding.run {
            pi = playlistWithInfo
            executePendingBindings()
        }
    }
}

class PlaylistExpandedViewHolder(binding: ListItemPlaylistBinding) : PlaylistViewHolder(binding) {

    override fun bind(playlistWithInfo: PlaylistWithInfo) {
        super.bind(playlistWithInfo)
        binding.motionLayout
            .setTransition(R.id.transition_playlist_end, R.id.transition_playlist_start)
    }

    companion object {
        fun from(parent: ViewGroup): PlaylistExpandedViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemPlaylistBinding.inflate(layoutInflater, parent, false)

            return PlaylistExpandedViewHolder(binding)
        }
    }
}

class PlaylistFoldedViewHolder(binding: ListItemPlaylistBinding) : PlaylistViewHolder(binding) {

    override fun bind(playlistWithInfo: PlaylistWithInfo) {
        super.bind(playlistWithInfo)
        binding.motionLayout
            .setTransition(R.id.transition_playlist_start, R.id.transition_playlist_end)
    }

    companion object {
        fun from(parent: ViewGroup): PlaylistFoldedViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemPlaylistBinding.inflate(layoutInflater, parent, false)

            return PlaylistFoldedViewHolder(binding)
        }
    }
}


class ExpansionViewHolder(val binding: ListItemPlaylistExpansionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(playlistWithItems: PlaylistExpansion) {
        binding.run {
            pi = playlistWithItems
            playlistItemsRv.adapter = PlaylistItemAdapter()
            executePendingBindings()
        }
    }

    companion object {
        fun from(parent: ViewGroup): ExpansionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemPlaylistExpansionBinding.inflate(layoutInflater, parent, false)

            return ExpansionViewHolder(binding)
        }
    }
}