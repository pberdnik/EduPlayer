package io.github.pberdnik.eduplayer.feat.explore.playlistrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.pberdnik.eduplayer.databinding.ListItemPlaylistBinding
import io.github.pberdnik.eduplayer.domain.PlaylistWithInfo


class PlaylistAdapter(private val onClickListener: PlaylistClickListener) :
    ListAdapter<PlaylistWithInfo, PlaylistViewHolder>(PlaylistDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlaylistViewHolder.from(parent)

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlistWithInfo = getItem(position)
        holder.itemView.setOnClickListener { onClickListener.onClick(playlistWithInfo) }
        holder.bind(playlistWithInfo)
    }
}


class PlaylistViewHolder(private val binding: ListItemPlaylistBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(pwi: PlaylistWithInfo) {
        binding.pwi = pwi
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): PlaylistViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemPlaylistBinding.inflate(layoutInflater, parent, false)

            return PlaylistViewHolder(binding)
        }
    }
}


class PlaylistDiffCallback : DiffUtil.ItemCallback<PlaylistWithInfo>() {

    override fun areItemsTheSame(oldItem: PlaylistWithInfo, newItem: PlaylistWithInfo) =
        oldItem.playlist.id == newItem.playlist.id

    override fun areContentsTheSame(oldItem: PlaylistWithInfo, newItem: PlaylistWithInfo) =
        oldItem == newItem
}


class PlaylistClickListener(val clickListener: (playlistWithInfo: PlaylistWithInfo) -> Unit) {
    fun onClick(playlistWithInfo: PlaylistWithInfo) = clickListener(playlistWithInfo)
}
