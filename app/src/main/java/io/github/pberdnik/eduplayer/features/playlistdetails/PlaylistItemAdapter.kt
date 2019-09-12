package io.github.pberdnik.eduplayer.features.playlistdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.pberdnik.eduplayer.databinding.ListItemPlaylistItemBinding
import io.github.pberdnik.eduplayer.domain.PlaylistItemWithInfo


class PlaylistItemAdapter(private val onClickListener: PlaylistItemClickListener) :
    ListAdapter<PlaylistItemWithInfo, PlaylistItemViewHolder>(PlaylistItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlaylistItemViewHolder.from(parent)

    override fun onBindViewHolder(holder: PlaylistItemViewHolder, position: Int) {
        val playlistItemWithInfo = getItem(position)
        holder.itemView.setOnClickListener { onClickListener.onClick(playlistItemWithInfo) }
        holder.bind(playlistItemWithInfo)
    }
}


class PlaylistItemViewHolder(private val binding: ListItemPlaylistItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(pwi: PlaylistItemWithInfo) {
        binding.playlistItem = pwi
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): PlaylistItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemPlaylistItemBinding.inflate(layoutInflater, parent, false)

            return PlaylistItemViewHolder(binding)
        }
    }
}


class PlaylistItemDiffCallback : DiffUtil.ItemCallback<PlaylistItemWithInfo>() {

    override fun areItemsTheSame(oldItem: PlaylistItemWithInfo, newItem: PlaylistItemWithInfo) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: PlaylistItemWithInfo, newItem: PlaylistItemWithInfo) =
        oldItem == newItem
}


class PlaylistItemClickListener(val clickListener: (playlistItemWithInfo: PlaylistItemWithInfo) -> Unit) {
    fun onClick(playlistItemWithInfo: PlaylistItemWithInfo) = clickListener(playlistItemWithInfo)
}
