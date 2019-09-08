package io.github.pberdnik.eduplayer.feat.explore.playlistrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.pberdnik.eduplayer.databinding.ListItemPlaylistExpandedBinding
import io.github.pberdnik.eduplayer.databinding.ListItemPlaylistExpansionBinding
import io.github.pberdnik.eduplayer.databinding.ListItemPlaylistFoldedBinding
import io.github.pberdnik.eduplayer.domain.PlaylistExpansion
import io.github.pberdnik.eduplayer.domain.PlaylistWithInfo


class PlaylistExpandedViewHolder(val binding: ListItemPlaylistExpandedBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(playlistWithInfo: PlaylistWithInfo) {
        binding.run {
            pi = playlistWithInfo
            executePendingBindings()
        }
    }

    companion object {
        fun from(parent: ViewGroup): PlaylistExpandedViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemPlaylistExpandedBinding.inflate(layoutInflater, parent, false)

            return PlaylistExpandedViewHolder(binding)
        }
    }
}


class PlaylistFoldedViewHolder(val binding: ListItemPlaylistFoldedBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(playlistWithInfo: PlaylistWithInfo) {
        binding.run {
            pi = playlistWithInfo
            executePendingBindings()
        }
    }

    companion object {
        fun from(parent: ViewGroup): PlaylistFoldedViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemPlaylistFoldedBinding.inflate(layoutInflater, parent, false)

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