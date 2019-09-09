package io.github.pberdnik.eduplayer.feat.explore

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.pberdnik.eduplayer.domain.PlaylistWithInfo
import io.github.pberdnik.eduplayer.feat.explore.playlistrecyclerview.PlaylistAdapter

@BindingAdapter("listData")
fun RecyclerView.bindListData(data: List<PlaylistWithInfo>?) {
    (adapter as PlaylistAdapter).submitList(data)
}
