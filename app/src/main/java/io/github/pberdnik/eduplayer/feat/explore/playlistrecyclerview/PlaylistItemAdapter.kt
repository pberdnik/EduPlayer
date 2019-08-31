package io.github.pberdnik.eduplayer.feat.explore.playlistrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.pberdnik.eduplayer.databinding.ListItemPlaylistItemBinding
import io.github.pberdnik.eduplayer.domain.PlaylistItemWithInfo

class PlaylistItemAdapter :
    ListAdapter<PlaylistItemWithInfo, PlaylistItemViewHolder>(
        PlaylistItemDiffCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistItemViewHolder =
        PlaylistItemViewHolder(
            ListItemPlaylistItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: PlaylistItemViewHolder, position: Int) =
        holder.bind(getItem(position))

}

class PlaylistItemViewHolder(private val binding: ListItemPlaylistItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(playlistItemWithInfo: PlaylistItemWithInfo) {
        binding.playlistItem = playlistItemWithInfo
        binding.executePendingBindings()
    }
}

class PlaylistItemDiffCallback : DiffUtil.ItemCallback<PlaylistItemWithInfo>() {
    override fun areItemsTheSame(oldItem: PlaylistItemWithInfo, newItem: PlaylistItemWithInfo) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: PlaylistItemWithInfo, newItem: PlaylistItemWithInfo) =
        oldItem == newItem
}