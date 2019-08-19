package io.github.pberdnik.eduplayer.feat.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.pberdnik.eduplayer.databinding.ListItemPlaylistBinding
import io.github.pberdnik.eduplayer.domain.Playlist

class PlaylistAdapter : ListAdapter<Playlist, PlaylistViewHolder>(PlaylistDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlaylistViewHolder(ListItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) =
        holder.bind(getItem(position))
}

class PlaylistViewHolder(private val binding: ListItemPlaylistBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(playlist: Playlist) {
        binding.motionLayout.run {
            progress = if (playlist.expended) 0f else 1f
            setOnClickListener {
                if (playlist.expended) transitionToEnd() else transitionToStart()
                playlist.expended = !playlist.expended
            }
        }
        binding.playlist = playlist
        binding.executePendingBindings()
    }
}

class PlaylistDiffCallback : DiffUtil.ItemCallback<Playlist>() {
    override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist) = oldItem == newItem
}
