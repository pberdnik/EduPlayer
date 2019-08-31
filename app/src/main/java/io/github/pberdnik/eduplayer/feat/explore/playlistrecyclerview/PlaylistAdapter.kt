package io.github.pberdnik.eduplayer.feat.explore.playlistrecyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.pberdnik.eduplayer.domain.PlaylistData
import io.github.pberdnik.eduplayer.domain.PlaylistExpansion
import io.github.pberdnik.eduplayer.domain.PlaylistWithInfo

const val PLAYLIST_EXPANDED = 0
const val PLAYLIST_FOLDED = 1
const val EXPANSION = 2

@Suppress("MemberVisibilityCanBePrivate")
class PlaylistAdapter(val onClickListener: PlaylistClickListener) :
    ListAdapter<PlaylistData, RecyclerView.ViewHolder>(PlaylistDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            PLAYLIST_EXPANDED -> PlaylistExpandedViewHolder.from(parent)
            PLAYLIST_FOLDED -> PlaylistFoldedViewHolder.from(parent)
            EXPANSION -> ExpansionViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val playlistData = getItem(position)
        if (playlistData is PlaylistWithInfo)
            holder.itemView.setOnClickListener { onClickListener.onClick(playlistData) }
        when (holder) {
            is PlaylistViewHolder -> {
                holder.itemView.setOnClickListener {
                    onClickListener.onClick(playlistData as PlaylistWithInfo)
                }
                holder.bind(playlistData as PlaylistWithInfo)
            }
            is ExpansionViewHolder ->
                holder.bind(playlistData as PlaylistExpansion)
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (val item = getItem(position)) {
            is PlaylistWithInfo -> if (item.expanded) PLAYLIST_EXPANDED else PLAYLIST_FOLDED
            is PlaylistExpansion -> EXPANSION
        }

}


class PlaylistDiffCallback : DiffUtil.ItemCallback<PlaylistData>() {
    override fun areItemsTheSame(oldItem: PlaylistData, newItem: PlaylistData) =
        oldItem.playlistId == newItem.playlistId && (
                oldItem is PlaylistWithInfo && newItem is PlaylistWithInfo ||
                        oldItem is PlaylistExpansion && newItem is PlaylistExpansion)


    override fun areContentsTheSame(oldItem: PlaylistData, newItem: PlaylistData) =
        oldItem == newItem
}

class PlaylistClickListener(
    val clickListener: (playlistWithInfo: PlaylistWithInfo) -> Unit
) {
    fun onClick(playlistWithInfo: PlaylistWithInfo) =
        clickListener(playlistWithInfo)
}
