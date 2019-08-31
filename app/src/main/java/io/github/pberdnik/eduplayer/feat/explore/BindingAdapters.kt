package io.github.pberdnik.eduplayer.feat.explore

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.pberdnik.eduplayer.domain.PlaylistData
import io.github.pberdnik.eduplayer.domain.PlaylistItemWithInfo
import io.github.pberdnik.eduplayer.feat.explore.playlistrecyclerview.PlaylistAdapter
import io.github.pberdnik.eduplayer.feat.explore.playlistrecyclerview.PlaylistItemAdapter

@BindingAdapter("listData")
fun RecyclerView.bindListData(data: List<PlaylistData>?) {
    (adapter as PlaylistAdapter).submitList(data)
}

@BindingAdapter("listData")
fun RecyclerView.bindPlaylistItemListData(data: List<PlaylistItemWithInfo>?) {
    (adapter as PlaylistItemAdapter).submitList(data)
}